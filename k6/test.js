import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL   = __ENV.BASE_URL   || 'http://localhost:8080';
const VUS        = Number(__ENV.VUS        || 40);
const DURATION   = __ENV.DURATION          || '2m';
const WRITE_SHARE = Number(__ENV.WRITE_SHARE || 50); // 5, 50, 95
const THINK_TIME  = Number(__ENV.THINK_TIME  || 0.3);

export const options = {
  scenarios: {
    mixed_profile: {
      executor: 'constant-vus',
      vus: VUS,
      duration: DURATION,
      gracefulStop: '10s',
      exec: 'mixedFlow',
      tags: {
        profile: `${WRITE_SHARE}/${100 - WRITE_SHARE}`,
      },
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)'],
  thresholds: {
    'http_req_duration{operation:create}': ['p(95)<500'],
    'http_req_duration{operation:read}':   ['p(95)<300'],
    'http_req_failed': ['rate<0.01'],
  },
};

export function mixedFlow() {
  if (Math.random() * 100 < WRITE_SHARE) {
    createStudent();
  } else {
    readStats();
  }
  sleep(THINK_TIME);
}

function createStudent() {
  const n = Math.random().toString(36).slice(2, 8);
  const payload = JSON.stringify({
    fio: `Student-${n}`,
    studentCard: `SC-${n}`,
    enrollmentYear: 2024,
  });

  const res = http.post(`${BASE_URL}/students/`, payload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { operation: 'create', entity: 'student' },
  });

  check(res, {
    'create: status 2xx':              (r) => r.status >= 200 && r.status < 300,
    'create: response time < 500ms':   (r) => r.timings.duration < 500,
  });
}

function readStats() {
  const res = http.get(`${BASE_URL}/enrollments/stats/average`, {
    tags: { operation: 'read', entity: 'stats' },
  });

  check(res, {
    'read: status 200':            (r) => r.status === 200,
    'read: has body':              (r) => r.body && r.body.length > 0,
    'read: response time < 300ms': (r) => r.timings.duration < 300,
  });
}