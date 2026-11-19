import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '5s', target: 5 },   // Ramp-up to 5 Virtual Users
    { duration: '10s', target: 20 }, // ISO 25010 target: 20 concurrent users
    { duration: '5s', target: 0 },   // Ramp-down to 0
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of HTTP requests must finish under 500ms
    http_req_failed: ['rate<0.05'],   // Error rate must be under 5%
  },
};

export default function () {
  // 1. Test Root Info Endpoint
  const resRoot = http.get('http://localhost:8081/');
  check(resRoot, {
    'root status is 200': (r) => r.status === 200,
    'root response time < 500ms': (r) => r.timings.duration < 500,
  });

  // 2. Test Actuator Health Endpoint
  const resHealth = http.get('http://localhost:8081/actuator/health');
  check(resHealth, {
    'health status is 200': (r) => r.status === 200,
    'health response time < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(0.5);
}
