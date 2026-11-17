import { test, expect } from '@playwright/test'

test.describe('Authentication E2E Flow', () => {
  test('should display the home dashboard', async ({ page }) => {
    await page.goto('/')
    await expect(page.getByRole('heading', { name: 'SpringZ Admin' }).first()).toBeVisible()
    await expect(page.getByText(/client dashboard/i)).toBeVisible()
  })

  test('should toggle between Sign in and Create account forms', async ({ page }) => {
    await page.goto('/auth')
    await expect(page.getByRole('heading', { name: 'Sign in' })).toBeVisible()

    // Switch to Sign Up
    await page.getByRole('button', { name: 'Need an account?' }).click()
    await expect(page.getByRole('heading', { name: 'Create account' })).toBeVisible()
    await expect(page.getByLabel('Name')).toBeVisible()

    // Switch back to Sign In
    await page.getByRole('button', { name: 'Already have an account?' }).click()
    await expect(page.getByRole('heading', { name: 'Sign in' })).toBeVisible()
  })

  test('should redirect unauthenticated users from protected pages to Auth page', async ({ page }) => {
    await page.goto('/categories')
    await expect(page).toHaveURL('/auth')
    await expect(page.getByRole('heading', { name: 'Sign in' })).toBeVisible()
  })
})
