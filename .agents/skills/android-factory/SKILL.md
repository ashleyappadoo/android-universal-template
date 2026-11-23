---
name: android-factory
description: Build or evolve an Android app from project-spec.yaml through verification and release preparation.
---

# Android Factory skill

Read `AGENTS.md`, `project-spec.yaml`, `factory/acceptance-criteria.yaml`, `PLANS.md`, and `docs/FACTORY.md`.

## Workflow
1. Run `python scripts/validate-project-spec.py`.
2. Identify incomplete release-blocking acceptance criteria.
3. Implement the smallest coherent set of changes.
4. Add tests and run `python scripts/factory-gate.py --stage ci`.
5. Run Android tests/lint/build.
6. Record evidence only when observed.
7. Keep `PLANS.md` and `factory/state.yaml` aligned with major milestones.
8. For release preparation run `python scripts/factory-gate.py --stage release`.
9. Never bypass protected production publishing.

A task is complete only when requested acceptance criteria pass with evidence and relevant deterministic gates are green.
