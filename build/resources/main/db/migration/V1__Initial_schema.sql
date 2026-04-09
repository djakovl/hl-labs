create table t_course (
    id uuid primary key default gen_random_uuid(),
    code varchar(50) not null unique,
    name varchar(255) not null,
    teacher varchar(255),
    year int not null default 2025,
    credits int,
    deleted boolean not null default false
);

create table t_student (
    id uuid primary key default gen_random_uuid(),
    fio varchar(255) not null,
    student_card varchar(100) not null unique,
    enrollment_year int not null,
    deleted boolean not null default false
);

create table t_enrollment (
    id uuid primary key default gen_random_uuid(),
    student_id uuid not null references t_student(id),
    course_id uuid not null references t_course(id),
    enrollment_date date not null,
    status varchar(50) not null,
    deleted boolean not null default false
);
