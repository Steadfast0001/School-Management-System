-- =====================================================
-- School / University Management System
-- PostgreSQL Refined Schema
-- =====================================================

-- =========================
-- ENUM TYPES
-- =========================
CREATE TYPE user_role AS ENUM ('ADMIN', 'TEACHER', 'STUDENT');
CREATE TYPE enrollment_status AS ENUM ('ACTIVE', 'COMPLETED', 'DROPPED');
CREATE TYPE fee_status AS ENUM ('PENDING', 'PAID', 'PARTIAL');
CREATE TYPE audience_type AS ENUM ('ALL', 'ADMIN', 'TEACHER', 'STUDENT');

-- =========================
-- USERS (AUTHENTICATION)
-- =========================
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- hashed (PBKDF2 / BCrypt)
    role varchar(50) NOT NULL DEFAULT 'STUDENT',
    name VARCHAR(150),
    email VARCHAR(255) UNIQUE,
    matricule VARCHAR(50) UNIQUE,
    level VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- CLASSES
-- =========================
CREATE TABLE IF NOT EXISTS classes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- STUDENTS
-- =========================
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    class_id INTEGER REFERENCES classes(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TEACHERS
-- =========================
CREATE TABLE IF NOT EXISTS teachers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- SUBJECTS
-- =========================
CREATE TABLE IF NOT EXISTS subjects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    teacher_id INTEGER REFERENCES teachers(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- ATTENDANCE
-- =========================
CREATE TABLE IF NOT EXISTS attendance (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    attendance_date DATE NOT NULL,
    present BOOLEAN NOT NULL DEFAULT TRUE,
    remarks TEXT,
    CONSTRAINT uq_attendance UNIQUE (student_id, attendance_date)
);

-- =========================
-- DEPARTMENTS
-- =========================
CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    department_code VARCHAR(10) UNIQUE NOT NULL,
    department_name VARCHAR(150) NOT NULL,
    description TEXT,
    head_of_department VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- COURSES
-- =========================
CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(150) NOT NULL,
    description TEXT,
    credits INTEGER NOT NULL CHECK (credits > 0),
    department_id INTEGER REFERENCES departments(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- ENROLLMENTS
-- =========================
CREATE TABLE IF NOT EXISTS enrollments (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    enrollment_date DATE DEFAULT CURRENT_DATE,
    status enrollment_status DEFAULT 'ACTIVE',
    grade DECIMAL(5,2) CHECK (grade BETWEEN 0 AND 100),
    CONSTRAINT uq_enrollment UNIQUE (student_id, course_id)
);

-- =========================
-- TIMETABLES
-- =========================
CREATE TABLE IF NOT EXISTS timetables (
    id SERIAL PRIMARY KEY,
    course_id INTEGER REFERENCES courses(id) ON DELETE CASCADE,
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    room VARCHAR(50) NOT NULL,
    instructor VARCHAR(150),
    CHECK (start_time < end_time)
);

-- =========================
-- LIBRARY
-- =========================
CREATE TABLE IF NOT EXISTS library (
    id SERIAL PRIMARY KEY,
    book_title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    category VARCHAR(50),
    total_copies INTEGER NOT NULL CHECK (total_copies >= 0),
    available_copies INTEGER NOT NULL CHECK (available_copies >= 0),
    location VARCHAR(100)
);

-- =========================
-- FEES
-- =========================
CREATE TABLE IF NOT EXISTS fees (
    id SERIAL PRIMARY KEY,
    student_id INTEGER REFERENCES students(id) ON DELETE CASCADE,
    fee_type VARCHAR(100) NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    paid_amount DECIMAL(10,2) DEFAULT 0 CHECK (paid_amount >= 0),
    due_date DATE NOT NULL,
    payment_date DATE,
    status fee_status DEFAULT 'PENDING'
);

-- =========================
-- REPORTS
-- =========================
CREATE TABLE IF NOT EXISTS reports (
    id SERIAL PRIMARY KEY,
    report_type VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    generated_date DATE DEFAULT CURRENT_DATE,
    generated_by VARCHAR(150) NOT NULL,
    data TEXT
);

-- =========================
-- ANNOUNCEMENTS
-- =========================
CREATE TABLE IF NOT EXISTS announcements (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(150) NOT NULL,
    publish_date DATE DEFAULT CURRENT_DATE,
    expiry_date DATE,
    target_audience audience_type DEFAULT 'ALL',
    is_active BOOLEAN DEFAULT TRUE
);

-- =========================
-- INDEXES (PERFORMANCE)
-- =========================
CREATE INDEX IF NOT EXISTS idx_students_class ON students(class_id);
CREATE INDEX IF NOT EXISTS idx_subjects_teacher ON subjects(teacher_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_student ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course ON enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_courses_department ON courses(department_id);
CREATE INDEX IF NOT EXISTS idx_fees_student ON fees(student_id);
CREATE INDEX IF NOT EXISTS idx_announcements_active ON announcements(is_active);

-- =====================================================
-- END OF SCHEMA
-- =====================================================
