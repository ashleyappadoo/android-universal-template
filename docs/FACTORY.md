# Android AI Factory v2

## Objective
A complete product brief becomes a structured specification, implemented by a coding agent, verified by deterministic CI, built as APK/AAB, published to Google Play Internal Testing, then promoted through a protected production rollout.

## Lifecycle

```text
Product brief
   ↓
Product agent
   ↓
project-spec.yaml + acceptance criteria
   ↓
Developer agent / Codex
   ↓
feature branch + PR
   ↓
Spec validation + CI
   ↓
release candidate
   ↓
signed AAB
   ↓
Google Play Internal
   ↓
human policy/release approval
   ↓
staged Production rollout
```

## Repository contracts
- `project-spec.yaml`: machine-readable product contract.
- `factory/acceptance-criteria.yaml`: observable definition of done.
- `AGENTS.md`: short agent navigation/rules file.
- `PLANS.md`: current long-running execution plan.
- `factory/state.yaml`: current factory phase and blockers.
- `policy/`: privacy, AdMob and Data Safety evidence.
- `distribution/`: Play Store/release metadata.

## Human gates
The factory deliberately does not silently approve legal declarations, Data Safety truthfulness, target audience, privacy/legal text, first-time Play Console account steps, or production rollout.

## Recommended states
`template → specified → building → qa → release-candidate → internal → production-approved → production`

Agents update state only when the corresponding deterministic gate has passed.

## Starting a real application
1. Normalize the cahier des charges into `project-spec.yaml`.
2. Run `bash scripts/bootstrap.sh "App Name" com.company.app com/company/app`.
3. Complete feature-specific acceptance criteria.
4. Replace policy/store placeholders.
5. Run `python scripts/validate-project-spec.py`.
6. Implement through PRs until release blockers have evidence.
7. Publish first to Internal Testing.
8. Approve Production only after real-device and policy review.

## Production protection
Create a GitHub Environment named `google-play-production` and configure required reviewers. Production publishing is manual and staged by default.
