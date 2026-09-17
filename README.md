# PornhubSearch Android

Android search helper project prepared for GitHub Actions.

## GitHub upload
1. Extract this ZIP.
2. Open your GitHub repository.
3. Upload the **contents of this folder** (not the ZIP itself) so `build.gradle.kts`, `settings.gradle.kts`, `app/`, and `.github/` are at the repository root.
4. Commit to the `main` branch.

## Build APK
The workflow `.github/workflows/build-apk.yml` runs automatically after a push to `main`, or can be started from GitHub Actions with **Run workflow**. It installs Gradle 8.11.1, builds the debug APK, and uploads `PornhubSearch-debug-apk` as an artifact.
