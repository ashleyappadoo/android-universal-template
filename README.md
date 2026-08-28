# Universal Android GitHub Template

Production-oriented Android starter designed to keep the development and release pipeline in GitHub:

- Kotlin + Jetpack Compose
- `compileSdk 36` / `targetSdk 36`
- GitHub Actions CI
- Debug APK artifact on every CI run
- Signed release APK + AAB
- Google Play **internal track** publishing workflow
- Google Mobile Ads **Next-Gen** scaffolding
- UMP privacy/consent flow
- `app-ads.txt` template
- Release-readiness policy gate
- Google Play Data Safety / privacy worksheets

## 1. Create a new app from this source

Run once after cloning:

```bash
./scripts/bootstrap.sh "My App" com.mycompany.myapp com/mycompany/myapp
```

Then replace the placeholder privacy/legal content and commit the result.

## 2. GitHub configuration

### Repository variables

Set under **Settings → Secrets and variables → Actions → Variables**:

- `ANDROID_APPLICATION_ID` — e.g. `com.mycompany.myapp`
- `ADMOB_APP_ID` — production AdMob App ID (`ca-app-pub-…~…`)
- `ADMOB_BANNER_AD_UNIT_ID` — production banner ad-unit ID (`ca-app-pub-…/…`)

AdMob IDs are identifiers rather than passwords, but keeping environment-specific values in GitHub Variables prevents accidental reuse between apps.

### Repository / environment secrets

- `ANDROID_KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`
- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`

Never commit the keystore, passwords, or Play service-account JSON.

## 3. Workflows

### `Android CI`
Runs on PRs and pushes to `main`:

1. unit tests
2. lint
3. debug APK build
4. APK uploaded as a GitHub Actions artifact

### `Android Release`
Runs manually or from a semantic version tag (`v1.2.0`):

1. release-readiness gate
2. release tests + lint
3. keystore reconstruction in the ephemeral runner
4. signed APK
5. signed AAB
6. R8 mapping file
7. GitHub artifact upload

### `Publish Google Play Internal`
Manual by design. It builds a signed AAB and uploads only to the Google Play **internal** track. Production promotion should remain a separate controlled step.

> Google Play API automation normally requires that the application/package has already been created in Play Console and initially configured.

## 4. AdMob / advertising readiness

This template intentionally uses Google test advertising identifiers only for the **debug** build. A release requires production IDs through GitHub variables.

The release gate checks that:

- package ID and app name were customized
- API 36 target is retained
- privacy/Data Safety/AdMob release files are present
- production AdMob identifiers have valid shapes
- Google test IDs are not hardcoded into `src/main`

The app requests UMP consent information on launch and initializes Mobile Ads only when `canRequestAds()` permits it.

### `app-ads.txt`

Edit `developer-site/app-ads.txt`, then publish it at the root of the exact developer-site domain referenced by Google Play:

```text
https://your-domain.example/app-ads.txt
```

Do not assume a GitHub Pages project subpath is equivalent to the root domain.

## 5. Release gate locally

```bash
export ADMOB_APP_ID='ca-app-pub-1234567890123456~1234567890'
export ADMOB_BANNER_AD_UNIT_ID='ca-app-pub-1234567890123456/1234567890'
./scripts/release-readiness.sh
```

The first run is expected to fail until you customize the package, app name and policy documents.

## 6. Build commands

```bash
gradle testDebugUnitTest lintDebug assembleDebug
gradle bundleRelease
```

For release builds, Gradle properties are supplied by the workflow through `ORG_GRADLE_PROJECT_*` environment variables.

## 7. Important policy boundary

“Ad-ready” does not mean automatic AdMob approval. Google still validates app ownership/readiness and policy compliance. The final app, store listing, privacy policy, Data Safety answers, audience settings and ad placements must all match the actual release.

## 8. Gradle execution model

GitHub Actions installs the pinned Gradle `8.13` distribution with `gradle/actions/setup-gradle`, so APK/AAB builds do **not** depend on Android Studio or a developer workstation. If you want a local Gradle Wrapper later, generate and commit it with `gradle wrapper --gradle-version 8.13`.
