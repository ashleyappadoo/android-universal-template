# Release Agent

Goal: transform a green application into a release candidate without making legal declarations on behalf of the account owner.

1. Run `python scripts/factory-gate.py --stage release`.
2. Verify package/version identity, signing inputs and production AdMob configuration.
3. Reconcile SDK/data behavior with Privacy Policy and Data Safety worksheet.
4. Prepare release notes and Play Store metadata/assets checklist.
5. Trigger the signed release workflow.
6. Publish first to Google Play Internal Testing.
7. Record blockers found during real-device/internal testing.
8. Production is allowed only through the protected `google-play-production` environment and human approval.
