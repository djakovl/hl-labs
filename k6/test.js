import http from 'k6/http';
import { sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const STUDENT_RATIO = parseFloat(__ENV.RATIO || '0.5');
const STATS_RATIO   = 1 - STUDENT_RATIO;
const MAX_VUS       = parseInt(__ENV.TARGET || '80');

const STAGES = (ratio) => [
  { duration: '10s', target: Math.round(10  * ratio) },
  { duration: '10s', target: Math.round(10  * ratio) },
  { duration: '10s', target: Math.round(20  * ratio) },
  { duration: '10s', target: Math.round(20  * ratio) },
  { duration: '10s', target: Math.round(40  * ratio) },
  { duration: '10s', target: Math.round(40  * ratio) },
  { duration: '10s', target: Math.round(MAX_VUS * ratio) },
  { duration: '10s', target: Math.round(MAX_VUS * ratio) },
  { duration: '10s', target: 0 },
];

export const options = {
  scenarios: {
    create_students: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 10 },
        { duration: '60s', target: 10 },
        { duration: '20s', target: 20 },
        { duration: '60s', target: 20 },
        { duration: '20s', target: 40 },
        { duration: '60s', target: 40 },
        { duration: '20s', target: 80 },
        { duration: '60s', target: 80 },
        { duration: '20s', target: 0 },
    ],
      exec: 'createStudent',
    },
    get_stats: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 10 },
        { duration: '60s', target: 10 },
        { duration: '20s', target: 20 },
        { duration: '60s', target: 20 },
        { duration: '20s', target: 40 },
        { duration: '60s', target: 40 },
        { duration: '20s', target: 80 },
        { duration: '60s', target: 80 },
        { duration: '20s', target: 0 },
    ],
      exec: 'getStats',
    },
  },
};

function makeStudent() {
  const n = Math.random().toString(36).slice(2, 8);
  return JSON.stringify({
    fio: `Student ${n}`,
    studentCard: `SC-${n}`,
    enrollmentYear: 2024,
  });
}

export function createStudent() {
  http.post(
    `${BASE_URL}/students/`,
    makeStudent(),
    { headers: { 'Content-Type': 'application/json' }, tags: { endpoint: 'student' } }
  );
  sleep(1);
}

export function getStats() {
  http.get(`${BASE_URL}/enrollments/stats/average`,{ tags: { endpoint: 'average' } });
  sleep(1);
}