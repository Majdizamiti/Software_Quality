import { test, expect } from '@playwright/test'

test.describe('Navigation & Dashboard E2E', () => {
  test('should navigate to Auth page via navigation bar', async ({ page }) => {
    await page.goto('/')
    await page.getByRole('link', { name: 'Sign in / Sign up' }).click()
    await expect(page).toHaveURL('/auth')
    await expect(page.getByRole('heading', { name: 'Sign in' })).toBeVisible()
  })

  test('should display resource cards on dashboard', async ({ page }) => {
    await page.goto('/')
    for (const title of ['Categories', 'Products', 'Customers', 'Orders']) {
      const card = page.locator('.stat-card').filter({
        has: page.locator('.stat-label', { hasText: new RegExp(`^${title}$`) })
      })
      await expect(card).toBeVisible()
    }
  })
})
