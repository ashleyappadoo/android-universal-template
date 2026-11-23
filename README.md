# Android AI Factory v2

An agent-first Android repository that turns a product brief into a verifiable application and carries it through CI, signed APK/AAB generation, Google Play Internal Testing, and a protected staged Production rollout.

## Target workflow

```text
Cahier des charges
        ↓
Product agent
        ↓
project-spec.yaml + acceptance criteria
        ↓
Codex / developer agent
        ↓
branches + PRs + tests
        ↓
GitHub Actions
        ↓
APK debug / signed APK + AAB
        ↓
Google Play Internal
        ↓
human policy + release approval
        ↓
staged Production rollout
```

The repository is the system of record. Agents do not rely on chat history to remember requirements.

## Agent-first files
- `AGENTS.md` — short repository contract and review rules.
- `project-spec.yaml` — machine-readable product/technical/privacy/publishing source of truth.
- `factory/acceptance-criteria.yaml` — definition of done with evidence.
- `PLANS.md` — active multi-phase execution plan.
- `docs/FACTORY.md` — lifecycle and approval gates.
- `.agents/skills/android-factory/SKILL.md` — reusable coding-agent workflow.

## Instantiate a project

```bash
pip install pyyaml jsonschema
bash scripts/bootstrap.sh "My App" com.mycompany.myapp com/mycompany/myapp
```

Then complete `project-spec.yaml`, replace policy/store placeholders and adapt acceptance criteria to the actual cahier des charges.

Validate:

```bash
python scripts/validate-project-spec.py
python scripts/factory-gate.py --stage ci
```

## Automated gates
Every PR/push to `main` validates the factory contract, runs unit tests + lint, builds an APK and uploads it as an artifact.

`android-release.yml` produces signed APK/AAB only after the strict release gate passes.

`google-play-internal.yml` publishes to Internal Testing.

`google-play-production.yml` performs a staged Production rollout through the protected GitHub Environment `google-play-production`.

## GitHub Variables
- `ANDROID_APPLICATION_ID`
- `ADMOB_APP_ID`
- `ADMOB_BANNER_AD_UNIT_ID`

## GitHub Secrets
- `ANDROID_KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`
- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`

Never commit these values.

## Human gates retained by design
The factory automates engineering and publishing mechanics, but keeps explicit approval for final privacy/Data Safety truthfulness, Play declarations, first-time account/console steps that require human action, and Production rollout.

See `docs/FACTORY.md` for the lifecycle.
