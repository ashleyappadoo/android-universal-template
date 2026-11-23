# Android AI Factory v2

## Objective
A complete product brief becomes a structured specification, is implemented by Codex, verified by deterministic CI, built as APK/AAB, published to Google Play Internal Testing, then promoted through a protected production rollout.

## Lifecycle

```text
PRODUCT_BRIEF.md
   ↓ Product Agent
project-spec.yaml + acceptance criteria + PLANS.md
   ↓ Codex Factory Build
workspace implementation + deterministic verification
   ↓
automated PR
   ↓
Spec validation + Android CI
   ↓
release candidate / signed AAB
   ↓
Google Play Internal
   ↓
human policy/release approval
   ↓
staged Production rollout
```

## Automation boundary
The official Codex GitHub Action is used only from a trusted manual dispatch on `main`. It receives a fixed repository-owned prompt, edits only the workspace, does not publish, and leaves commit/push/PR creation to deterministic workflow steps.

The required secret is `OPENAI_API_KEY`.

## Repository contracts
- `PRODUCT_BRIEF.md`: raw human product requirements.
- `project-spec.yaml`: machine-readable product contract.
- `factory/acceptance-criteria.yaml`: observable definition of done.
- `AGENTS.md`: agent rules and map.
- `PLANS.md`: long-running execution plan.
- `factory/state.yaml`: phase and blockers.
- `policy/`: privacy, AdMob and Data Safety evidence.
- `distribution/`: Play Store/release metadata.

## Human gates
The factory deliberately does not silently approve legal declarations, Data Safety truthfulness, target audience, privacy/legal text, first-time Play Console account steps, or production rollout.

## Recommended states
`template → specified → building → qa → release-candidate → internal → production-approved → production`

## Starting a real application
1. Put the full brief in `PRODUCT_BRIEF.md`.
2. Product Agent updates spec, acceptance criteria and plan.
3. Bootstrap the real app identity.
4. Add `OPENAI_API_KEY` and run **Codex Factory Build**.
5. Review/merge generated PRs as CI turns green.
6. Complete policy/store metadata and production IDs.
7. Build release candidate and publish Internal.
8. Validate on real devices.
9. Approve protected staged Production rollout.

## Production protection
Create the GitHub Environment `google-play-production` with required reviewers. Production publishing is manual and staged by default.
