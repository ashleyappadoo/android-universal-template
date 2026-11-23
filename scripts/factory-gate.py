#!/usr/bin/env python3
import argparse
import subprocess
import sys
from pathlib import Path
import yaml

ROOT = Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser()
parser.add_argument("--stage", choices=["spec","ci","release","internal","production"], required=True)
args = parser.parse_args()

subprocess.run([sys.executable, str(ROOT / "scripts/validate-project-spec.py")], check=True)
spec = yaml.safe_load((ROOT / "project-spec.yaml").read_text(encoding="utf-8"))
acceptance = yaml.safe_load((ROOT / "factory/acceptance-criteria.yaml").read_text(encoding="utf-8"))
failures = []

for relative in ["AGENTS.md","PLANS.md","project-spec.yaml","factory/acceptance-criteria.yaml","policy/PRIVACY_POLICY.md","policy/GOOGLE_PLAY_DATA_SAFETY.md"]:
    if not (ROOT / relative).is_file():
        failures.append(f"missing required file: {relative}")

valid_status = {"todo","in_progress","passed","blocked"}
criteria = acceptance.get("criteria", [])
for criterion in criteria:
    if criterion.get("status") not in valid_status:
        failures.append(f"{criterion.get('id','<unknown>')} has invalid status")
    if criterion.get("status") == "passed" and not criterion.get("evidence"):
        failures.append(f"{criterion.get('id')} is passed without evidence")

strict = args.stage in {"release","internal","production"}
if strict:
    if spec["factory"]["template_mode"]:
        failures.append("template_mode must be false before a release candidate")
    for criterion in criteria:
        if criterion.get("release_blocking") and criterion.get("status") != "passed":
            failures.append(f"release blocker not passed: {criterion.get('id')} — {criterion.get('title')}")

    for path in ["policy/PRIVACY_POLICY.md","distribution/store-listing.yaml"]:
        p = ROOT / path
        if not p.is_file():
            failures.append(f"missing release metadata: {path}")
        else:
            content = p.read_text(encoding="utf-8")
            if "TODO_" in content or "[APP NAME]" in content:
                failures.append(f"release metadata still contains placeholders: {path}")

    if spec["monetization"]["ads"]["enabled"]:
        if not (ROOT / "developer-site/app-ads.txt").is_file():
            failures.append("ads enabled but developer-site/app-ads.txt is missing")
        else:
            subprocess.run(["bash", str(ROOT / "scripts/release-readiness.sh")], check=True)

if args.stage == "production":
    production = spec["publishing"]["google_play"]["production"]
    if not production.get("enabled"):
        failures.append("production publishing is disabled")
    if not production.get("require_human_approval"):
        failures.append("production must keep require_human_approval=true")

if failures:
    for item in failures:
        print(f"❌ {item}")
    sys.exit(1)

print(f"✅ Android AI Factory gate passed: {args.stage}")
