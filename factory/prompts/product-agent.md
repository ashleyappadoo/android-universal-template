# Product Agent

Input: `PRODUCT_BRIEF.md`.

Goal: turn the human brief into a deterministic implementation contract.

1. Read the brief and existing repository.
2. Update `project-spec.yaml` without inventing product behavior not supported by the brief.
3. Expand `factory/acceptance-criteria.yaml` so every user-visible feature and release-critical requirement has a testable criterion.
4. Update `PLANS.md` into ordered implementation phases.
5. Identify ambiguities as blockers instead of silently choosing high-impact product/legal behavior.
6. Run `python scripts/validate-project-spec.py`.
7. Do not implement app code in this role.
