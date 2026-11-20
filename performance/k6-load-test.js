import http from 'k6/http';
import { check, sleep } from 'k6';

// Set BASE_URL when k6 runs in Docker, for example:
// BASE_URL=http://host.docker.internal:8081
const baseUrl = __ENV.BASE_URL || 'http://localhost:8081';

export const options = {
  stages: [
    { duration: '10s', target: 10 }, // Ramp-up to 10 Virtual Users
    { duration: '20s', target: 20 }, // Stay at 20 VUs (ISO 25010 requirement: 20 concurrent users)
    { duration: '10s', target: 0 },  // Ramp-down to 0
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of HTTP requests must finish under 500ms
    http_req_failed: ['rate<0.01'],   // HTTP error rate must be below 1%
  },
};

export default function () {
  // 1. Test Root Info Endpoint
  const resRoot = http.get(`${baseUrl}/`);
  check(resRoot, {
    'root status is 200': (r) => r.status === 200,
    'root response time < 500ms': (r) => r.status === 200 && r.timings.duration < 500,
  });

  // 2. Test Actuator Health Endpoint
  const resHealth = http.get(`${baseUrl}/actuator/health`);
  check(resHealth, {
    'health status is 200': (r) => r.status === 200,
  });

  // 3. Test API User Registration Load
  const uniqueEmail = `k6.user.${Math.random().toString(36).substring(7)}@example.com`;
  const signupPayload = JSON.stringify({
    name: 'k6 Load User',
    email: uniqueEmail,
    password: 'password123',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const resSignup = http.post(`${baseUrl}/api/auth/signup`, signupPayload, params);
  check(resSignup, {
    'signup status is 200': (r) => r.status === 200,
    'signup response time < 500ms': (r) => r.status === 200 && r.timings.duration < 500,
  });

  sleep(1);
}
