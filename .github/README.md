# GitHub Configuration

This directory contains GitHub-specific configuration for the MenzaNS project.

## Contents

### Workflows (`.github/workflows/`)

- **`android-build.yml`** - Android app CI/CD pipeline
  - Triggers on PR and push to `master`/`develop`
  - Builds debug APK
  - Runs unit tests
  - Runs linting checks
  - Reports results on PRs

### Templates (`.github/ISSUE_TEMPLATE/`)

- **`bug_report.md`** - Template for reporting bugs
- **`feature_request.md`** - Template for requesting features
- **`config.yml`** - Configuration for issue templates

### Pull Request Template

- **`PULL_REQUEST_TEMPLATE.md`** - Standard template for all PRs

## CI/CD Pipelines

### Android App (MenzaNS-app)

**Trigger**: On every PR and push to `master`/`develop` branches

**Steps**:
1. Checkout code
2. Setup Java 17
3. Build debug APK (`./gradlew assembleDebug`)
4. Run unit tests (`./gradlew testDebugUnitTest`)
5. Run linting (`./gradlew lint`)
6. Report test results
7. Upload build artifacts on failure
8. Comment on PR with build status

**Status Badge**:
```markdown
[![Android Build & Test](https://github.com/pappt04/MenzaNS/actions/workflows/android-build.yml/badge.svg)](https://github.com/pappt04/MenzaNS/actions/workflows/android-build.yml)
```

## Issue Templates

When creating a new issue, you'll be prompted to choose from:

1. **Bug Report** - For reporting bugs with:
   - Component affected
   - Environment details
   - Steps to reproduce
   - Expected vs actual behavior
   - Severity level

2. **Feature Request** - For suggesting features with:
   - Component target
   - Problem statement
   - Proposed solution
   - User stories
   - Acceptance criteria
   - Priority level

## Contributing

When submitting a PR:

1. Use the provided PR template
2. Select the type of change
3. Link related issues
4. Ensure all tests pass locally
5. Wait for CI/CD to complete
6. Address any review feedback

## Secrets & Configuration

No secrets are currently stored in GitHub Actions.

For sensitive data (API keys, signing keys):
- Store in GitHub Secrets (requires admin access)
- Reference as `${{ secrets.SECRET_NAME }}` in workflows
- Never commit credentials to the repository

## Maintenance

- Review workflows quarterly for deprecated actions
- Update Java version in workflows if needed
- Keep dependency versions current
- Monitor action versions for security updates

## References

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Issue Templates](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests)
- [Workflow Syntax](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)
