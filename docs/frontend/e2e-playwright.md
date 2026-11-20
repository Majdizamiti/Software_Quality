# Frontend End-to-End Tests with Playwright

The frontend has five Playwright tests in `frontend/e2e/`. They cover the dashboard, sign-in/sign-up form toggle, redirect from a protected page, navigation to authentication, and dashboard resource cards.

## Requirements

- Node.js and npm.
- Playwright Chromium browser. The Playwright configuration starts Vite automatically on port 5173.

## Install and run

```powershell
cd frontend
npm ci
npx playwright install chromium
npm run test:e2e
```

The test command runs headless and creates an HTML report. To open it:

```powershell
npx playwright show-report
```

To run with the interactive Playwright UI:

```powershell
npx playwright test --ui
# or
npm run test:e2e -- --ui
#or
npx --prefix frontend playwright test --ui
```

The current configuration tests Chromium only. Add and run additional browser projects before claiming cross-browser compatibility. Save the actual test output and report after each run.

The frontend currently has no separate unit-test script in `package.json`; `npm run build` checks that Vite can compile the app, while `npm run test:e2e` runs the browser tests.
