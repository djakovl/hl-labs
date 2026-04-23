import argparse
import requests
from faker import Faker
import random

fake = Faker('ru_RU')
BASE_URL = "http://localhost:8080"

# ── Генераторы ───────────────────────────────────────────────────

def make_student():
    return {
        "fio":            fake.last_name() + " " + fake.first_name() + " " + fake.middle_name(),
        "studentCard":    f"SC-{fake.unique.random_number(digits=6)}",
        "enrollmentYear": fake.random_int(min=2018, max=2024),
    }

def make_course():
    subjects = ["Математика", "Физика", "Информатика", "История", "Химия",
                "Биология", "Философия", "Экономика", "Право", "Английский"]
    return {
        "code":    fake.bothify("??-###").upper(),
        "name":    random.choice(subjects),
        "teacher": fake.last_name() + " " + fake.first_name()[0] + "." + fake.middle_name()[0] + ".",
        "credits": random.randint(2, 6),
        "year":    2026,
    }

# ── Helpers ──────────────────────────────────────────────────────

def clear(endpoint):
    r = requests.delete(f"{BASE_URL}/{endpoint}/clear")
    print(f"[CLEAR] /{endpoint}/clear → {r.status_code}")

def create_many(endpoint, generator, count):
    ids = []
    for i in range(count):
        r = requests.post(f"{BASE_URL}/{endpoint}/", json=generator())
        if r.status_code == 200:
            ids.append(r.json().get("id"))
        else:
            print(f"[WARN] POST /{endpoint}/ #{i} → {r.status_code}: {r.text}")
        if (i + 1) % 50 == 0:
            print(f"  {i + 1}/{count} создано в /{endpoint}...")
    print(f"[OK] {len(ids)}/{count} записей в /{endpoint}")
    return ids

# ── Сидеры ───────────────────────────────────────────────────────

def seed_students(count, no_clear):
    if not no_clear:
        clear("enrollments")
        clear("students")
    create_many("students", make_student, count)

def seed_courses(count, no_clear):
    if not no_clear:
        clear("enrollments")
        clear("courses")
    create_many("courses", make_course, count)

def seed_enrollments(count, no_clear):
    student_count = max(10, count // 5)
    course_count  = max(5,  count // 10)

    if not no_clear:
        clear("enrollments")
        clear("students")
        clear("courses")

    student_ids = create_many("students", make_student, student_count)
    course_ids  = create_many("courses",  make_course,  course_count)
    
    if not student_ids or not course_ids:
        print("[ERROR] Нет студентов или курсов")
        return

    ok = 0
    for i in range(count):
        r = requests.post(f"{BASE_URL}/enrollments/", json={
            "studentId": fake.random_element(student_ids),
            "courseId":  fake.random_element(course_ids),
        })       
        if r.status_code == 200:
            ok += 1
        else:
            if i < 3:  # печатаем только первые 3 ошибки
                print(f"[WARN] enrollment #{i} → {r.status_code}: {r.text}")
        if (i + 1) % 50 == 0:
            print(f"  {i + 1}/{count} enrollment создано...")
    print(f"[OK] {ok}/{count} enrollment-записей")

SEEDERS = {
    "students":    seed_students,
    "courses":     seed_courses,
    "enrollments": seed_enrollments,
}

# ── CLI ──────────────────────────────────────────────────────────

CLEAR_TARGETS = {
    "students":    ["enrollments", "students"],
    "courses":     ["enrollments", "courses"],
    "enrollments": ["enrollments"],
}

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--count",    type=int, default=500)
    parser.add_argument("--endpoint", type=str, default=None,
                        choices=SEEDERS.keys())
    parser.add_argument("--no-clear", action="store_true",
                        help="Не очищать перед сидированием")
    parser.add_argument("--clear",    action="store_true",
                        help="Очистить БД. Без --endpoint — всё, с --endpoint — только связанные таблицы")
    args = parser.parse_args()

    if args.clear:
        targets = CLEAR_TARGETS[args.endpoint] if args.endpoint else ["enrollments", "students", "courses"]
        for ep in targets:
            clear(ep)
    else:
        if args.endpoint is None:
            args.endpoint = "enrollments"  # дефолт только для сидирования
        SEEDERS[args.endpoint](args.count, args.no_clear)