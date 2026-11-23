#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path

import yaml
from jsonschema import Draft202012Validator

ROOT = Path(__file__).resolve().parents[1]
spec = yaml.safe_load((ROOT / "project-spec.yaml").read_text(encoding="utf-8"))
schema = json.loads((ROOT / "factory/project-spec.schema.json").read_text(encoding="utf-8"))

errors = sorted(Draft202012Validator(schema).iter_errors(spec), key=lambda e: list(e.path))
if errors:
    for error in errors:
        location = ".".join(str(p) for p in error.path) or "<root>"
        print(f"❌ {location}: {error.message}")
    sys.exit(1)

failures = []
android = spec["android"]
product = spec["product"]
factory = spec["factory"]
ads = spec["monetization"]["ads"]

if android["compile_sdk"] < android["target_sdk"]:
    failures.append("compile_sdk must be >= target_sdk")
if not re.match(r"^[A-Za-z][A-Za-z0-9_]*(\.[A-Za-z][A-Za-z0-9_]*){1,}$", product["package_name"]):
    failures.append("invalid Android package_name")
if ads["enabled"] and ads.get("provider") != "admob":
    failures.append("ads.enabled=true currently requires provider=admob")
if not factory["template_mode"]:
    if product["package_name"] == "com.example.universal":
        failures.append("real projects must replace com.example.universal")
    if product["name"] == "Universal Android App":
        failures.append("real projects must replace the template app name")

if failures:
    for item in failures:
        print(f"❌ {item}")
    sys.exit(1)

mode = "TEMPLATE" if factory["template_mode"] else "PROJECT"
print(f"✅ project-spec.yaml valid ({mode})")
print(f"   {product['name']} | {product['package_name']} | targetSdk {android['target_sdk']}")
