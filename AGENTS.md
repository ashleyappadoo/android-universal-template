# Android AI Factory — Agent contract

This repository is an agent-first Android application factory.

## Source of truth
Read these in order before changing code:
1. `project-spec.yaml` — product, technical, privacy, monetization and publishing contract.
2. `factory/acceptance-criteria.yaml` — verifiable definition of done.
3. `PLANS.md` — active multi-step execution plan.
4. `docs/FACTORY.md` — lifecycle and human approval gates.
5. Relevant source/tests.

Keep this file short. Put durable detail in the referenced files.

## Work loop
1. Validate the repository state.
2. Update the plan for multi-step work.
3. Implement the smallest coherent slice.
4. Add/update tests.
5. Run deterministic gates.
6. Mark acceptance criteria passed only from observed evidence.
7. Open/update a PR with scope, evidence, risks and blockers.

## Required verification
- `python scripts/validate-project-spec.py`
- `python scripts/factory-gate.py --stage ci`
- `gradle testDebugUnitTest lintDebug assembleDebug --stacktrace`

For release candidates:
- `python scripts/factory-gate.py --stage release`
- release tests/lint/build through GitHub Actions.

## Hard rules
- Never commit secrets, keystores, service-account JSON or API keys.
- Never weaken tests to make CI green.
- Never mark acceptance criteria passed without evidence.
- Keep Google sample ad IDs in debug only.
- Privacy Policy and Data Safety must match final code/SDK inventory.
- Production publishing must use the protected `google-play-production` environment.
- Legal/policy declarations and production rollout remain human approval gates.
- Prefer PRs over direct changes to `main`.

## Review rules
Flag changes that add data collection without policy updates, bypass consent/release gates, hardcode credentials, unexpectedly change app identity, lower the API target, or weaken production rollout protection.
