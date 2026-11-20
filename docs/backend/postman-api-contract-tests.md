# API Contract Tests with Postman

Use `postman/SpringZ_API.postman_collection.json` for the full API request set. It now includes Postman test scripts for HTTP status codes and response shapes on every request, plus variable handling for auth and resource IDs. The focused collection `postman/SpringZ_API_Contract_Tests.postman_collection.json` remains available for a shorter nine-request auth/category run.

The full collection contains these request groups:

- Auth: signup and signin
- Seven resource groups: list, create, read, update
- System Info and Health Checks
- Cleanup: delete created records in reverse dependency order

The resource create requests capture their generated IDs for subsequent read/list/update checks. Create and update scripts compare returned field values with the values in each submitted request body. The final Cleanup folder removes those records after the runner has finished using them.

## Run the full collection

1. Make sure SpringZ is running at `http://localhost:8081` against a disposable test database.
2. In Postman, import `postman/SpringZ_API.postman_collection.json`. Re-import the file after collection updates so the new scripts are loaded.
3. Confirm `baseUrl` is `http://localhost:8081` and run the entire collection once in Collection Runner.
4. Review the test results for every request and confirm the runner reports zero failures.

The full collection creates signup and resource records, then cleans up the resource records in the final folder. Use a test database; don't run it against production.

## Focused auth and category flow

The smaller `postman/SpringZ_API_Contract_Tests.postman_collection.json` contains nine sequential requests with assertions for HTTP status codes and JSON response fields:

1. Sign up with a unique email.
2. Sign in and save the JWT.
3. Confirm the categories API rejects a request without a JWT.
4. List categories with the JWT.
5. Create a category and save its ID.
6. Read the created category.
7. Update the category.
8. Delete the category.
9. Confirm the deleted category returns the standard 404 error response.

## Before you run

- Start SpringZ at `http://localhost:8081` and connect it to a disposable test database. See the database/backend setup in [the k6 guide](performance-k6.md); you do not need to run k6 for these tests.
- Do not run the collection against production. Signup creates a user; category creation is deleted at the end.

## Import and run the focused flow

1. Open Postman and choose **Import**.
2. Select `postman/SpringZ_API_Contract_Tests.postman_collection.json` from the project.
3. Confirm the collection variable `baseUrl` is `http://localhost:8081`.
4. Open the collection's **Run** option. Keep the requests in their listed order and use one iteration.
5. Start the run and review the **Test Results**. Each request should have passing assertions and the final summary should show zero failed tests.

The collection generates a new email address on each run, captures the JWT from signin, and captures the category ID from the create response. It does not need the existing `SpringZ API` collection's fixed IDs.

Save the Postman Runner summary with the date and environment as API contract evidence. The original `SpringZ_API.postman_collection.json` is a broader request collection; this focused collection adds explicit status and response-shape assertions for its flow.

## Endpoint inventory

The original `SpringZ API` collection contains signup/signin and list/get/create/update/delete requests for Categories, Subcategories, Providers, Customers, Users, Products, and Orders. Comparing those requests with the backend mappings found these two omissions; they have now been added to separate top-level folders in that collection:

| Folder | Method and endpoint | Purpose |
|---|---|---|
| `System Info` | `GET /` | Returns service status, application name, message, and endpoint links |
| `Health Checks` | `GET /actuator/health` | Returns the Spring Boot health status |

No other route mappings from the current API controllers were missing from the original request collection. The focused contract collection is separate so its sequential assertions can be run without the broader collection's fixed resource IDs.
