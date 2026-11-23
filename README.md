# Android AI Factory v2

An agent-first Android repository that turns a product brief into a verifiable application and carries it through coding, CI, signed APK/AAB generation, Google Play Internal Testing, and a protected staged Production rollout.

## Target workflow

```text
Cahier des charges / PRODUCT_BRIEF.md
        ↓
Product Agent → project-spec.yaml + acceptance criteria
        ↓
Codex Factory Build
        ↓
implementation + tests + PR
        ↓
GitHub Actions CI
        ↓
APK debug / signed APK + AAB
        ↓
Google Play Internal
        ↓
human policy + release approval
        ↓
staged Production rollout
```

The repository is the system of record; agents do not rely on chat history to remember requirements.

## Agent-first files
- `PRODUCT_BRIEF.md` — human cahier des charges inbox.
- `AGENTS.md` — repository contract and review rules.
- `project-spec.yaml` — machine-readable source of truth.
- `factory/acceptance-criteria.yaml` — definition of done with evidence.
- `PLANS.md` — active phased implementation plan.
- `.agents/skills/android-factory/SKILL.md` — reusable Codex workflow.
- `docs/FACTORY.md` — lifecycle and approval gates.

## From brief to coding PR
After the Product Agent has normalized the brief/spec, run **Actions → Codex Factory Build** on `main`.

It supports:
- `next_phase` — implement the next incomplete phase;
- `full_app` — iterate through as much of the app as can be verified before a human gate.

The workflow uses the official `openai/codex-action`, runs deterministic validation/tests after the agent, and opens a PR only if verified code changes exist.

Add the GitHub secret:
- `OPENAI_API_KEY`

## Instantiate a project

```bash
pip install pyyaml jsonschema
bash scripts/bootstrap.sh "My App" com.mycompany.myapp com/mycompany/myapp
```

Then complete `project-spec.yaml`, replace policy/store placeholders and adapt acceptance criteria to the real brief.

## Automated gates
Every PR validates the factory contract, runs unit tests + lint, builds an APK and uploads it as an artifact.

`android-release.yml` produces signed APK/AAB after the strict release gate.

`google-play-internal.yml` publishes to Internal Testing.

`google-play-production.yml` performs a staged Production rollout through the protected GitHub Environment `google-play-production`.

## GitHub Variables
- `ANDROID_APPLICATION_ID`
- `ADMOB_APP_ID`
- `ADMOB_BANNER_AD_UNIT_ID`

## GitHub Secrets
- `OPENAI_API_KEY`
- `ANDROID_KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`
- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`

## Human gates retained by design
The factory automates product-to-code mechanics and publishing mechanics, but keeps explicit approval for final privacy/Data Safety truthfulness, Play legal declarations, first-time account/console steps that require human action, and Production rollout.

See `docs/FACTORY.md`.
