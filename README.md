# School-Management-System
this is a group project made up of three(3) students NKENGANYI STEADFAST BEKWIKE(B.Eng) [](https://github.com/Steadfast0001) MENSAH ASHLEY(B.Tech)[](https://github.com/Ashley2377) and HONGLA BRANDON(B.Tech)[](https://github.com/cisbren)

# University Management System

A comprehensive Java Swing application for managing university operations including student enrollment, course management, faculty administration, and academic records.

## Features

### Core Functionality
- **Secure Authentication**: Role-based access control (Admin, Faculty, Student)
- **User Management**: Complete CRUD operations for users, students, and faculty
- **Course Management**: Create, update, and manage courses with department associations
- **Department Management**: Organize academic departments and their faculty
- **Enrollment System**: Student course registration and enrollment tracking
- **Timetable Management**: Schedule courses and manage class timings
- **Library Management**: Book cataloging and borrowing system
- **Fee Management**: Tuition and fee tracking for students
- **Announcement System**: University-wide and targeted announcements
- **Reporting**: Generate various academic and administrative reports

### Technical Features
- **Database**: PostgreSQL with comprehensive schema
- **Security**: PBKDF2 password hashing with salt
- **Configuration**: Externalized settings with environment variable support
- **Validation**: Input validation and data integrity checks
- **Modern UI**: Java Swing with enhanced styling and user experience

## Project Structure

```
src/
├── main/
│   ├── app/
│   │   ├── DBSeeder.java          # Database initialization
│   │   ├── MainApp.java           # Application entry point
│   │   └── RunSeeder.java         # Seeder runner
│   ├── config/
│   │   └── DBConnection.java      # Database connection management
│   ├── dao/                       # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── StudentDAO.java
│   │   ├── TeacherDAO.java
│   │   ├── CourseDAO.java
│   │   ├── DepartmentDAO.java
│   │   ├── EnrollmentDAO.java
│   │   ├── TimetableDAO.java
│   │   ├── LibraryDAO.java
│   │   ├── FeeDAO.java
│   │   ├── AnnouncementDAO.java
│   │   └── ReportDAO.java
│   ├── model/                     # Data models
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Teacher.java
│   │   ├── Course.java
│   │   ├── Department.java
│   │   ├── Enrollment.java
│   │   ├── Timetable.java
│   │   ├── Library.java
│   │   ├── Fee.java
│   │   ├── Announcement.java
│   │   └── Report.java
│   ├── service/                   # Business logic
│   │   ├── AuthService.java
│   │   ├── StudentService.java
│   │   └── TeacherService.java
│   ├── ui/                        # User interface frames
│   │   ├── LoginFrame.java
│   │   ├── DashboardFrame.java
│   │   ├── StudentFrame.java
│   │   ├── TeacherFrame.java
│   │   ├── CourseFrame.java
│   │   ├── DepartmentFrame.java
│   │   ├── EnrollmentFrame.java
│   │   ├── TimetableFrame.java
│   │   ├── LibraryFrame.java
│   │   ├── FeeFrame.java
│   │   ├── AnnouncementFrame.java
│   │   ├── ReportFrame.java
│   │   ├── ResultFrame.java
│   │   └── AttendanceFrame.java
│   └── utils/                     # Utility classes
│       ├── PasswordUtil.java
│       ├── Validator.java
│       └── AppConfig.java
└── test/
    └── java/                      # Test classes
        ├── SystemTestSuite.java
        ├── AuthServiceTest.java
        ├── UserDAOTest.java
        ├── DepartmentDAOTest.java
        ├── CourseDAOTest.java
        ├── StudentDAOTest.java
        ├── ValidatorTest.java
        └── PasswordUtilTest.java
```

## Prerequisites

- **Java**: JDK 11 or higher
- **PostgreSQL**: Version 12 or higher
- **Maven**: For dependency management (optional, can use included libraries)

## Installation & Setup

### 1. Database Setup

1. Install PostgreSQL and create a database:
```sql
CREATE DATABASE university_management;
```

2. Run the database schema:
```bash
psql -d university_management -f database_setup_postgresql.sql
```

### 2. Configuration

1. **Environment Variables** (Recommended for production):
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=university_management
DB_USER=your_username
DB_PASSWORD=your_password

# Application Configuration
APP_NAME=University Management System
APP_VERSION=1.0.0
LOG_LEVEL=INFO
```

2. **Properties Files** (Fallback configuration):
- `app.properties`: Application settings
- `database.properties`: Database connection settings

### 3. Build and Run

#### Option A: Using included libraries
```bash
# Compile the application
javac -cp "lib/*" -d bin src/main/app/*.java src/main/config/*.java src/main/dao/*.java src/main/model/*.java src/main/service/*.java src/main/ui/*.java src/main/utils/*.java

# Run the application
java -cp "bin:lib/*" main.app.MainApp
```

#### Option B: Using Maven (if you set up Maven)
```bash
# Add dependencies to pom.xml and run
mvn clean compile exec:java -Dexec.mainClass="main.app.MainApp"
```

### 4. Initialize Database

Run the database seeder to populate initial data:
```bash
java -cp "bin:lib/*" main.app.RunSeeder
```

## Usage

### First Time Setup
1. Start the application
2. Use default admin credentials:
   - Username: `admin`
   - Password: `admin123`
3. Change the default password immediately
4. Create departments, courses, and users

### User Roles

- **Admin**: Full system access, user management, system configuration
- **Faculty**: Course management, student grading, attendance
- **Student**: Course enrollment, view grades, access resources

### Key Workflows

#### Student Management
1. Create student profile
2. Enroll in courses
3. View timetable and grades
4. Access library resources
5. Pay fees and view financial status

#### Course Management
1. Create/Edit courses with department association
2. Set prerequisites and credit hours
3. Manage enrollments and capacity
4. Schedule classes in timetable

#### Faculty Operations
1. View assigned courses
2. Take attendance
3. Enter grades
4. Access student information

## Testing

Run the comprehensive test suite:

```bash
# Run all tests
java -cp "bin:lib/*" test.java.SystemTestSuite

# Run individual test classes
java -cp "bin:lib/*" test.java.AuthServiceTest
java -cp "bin:lib/*" test.java.UserDAOTest
java -cp "bin:lib/*" test.java.ValidatorTest
# ... etc
```

### Test Coverage
- Authentication and authorization
- CRUD operations for all entities
- Input validation
- Password security
- Database operations
- Edge cases and error handling

## Configuration

### AppConfig.java
Centralized configuration management with support for:
- Environment variables (highest priority)
- Properties files (fallback)
- Default values (lowest priority)

### Database Configuration
- Connection pooling support
- Automatic reconnection
- Query timeout settings
- Connection validation

## Security Features

- **Password Hashing**: PBKDF2 with random salt
- **Role-Based Access**: Admin, Faculty, Student roles
- **Input Validation**: Comprehensive validation for all inputs
- **SQL Injection Prevention**: Prepared statements
- **Session Management**: Secure user sessions

## Database Schema

### Core Tables
- `users`: User accounts and authentication
- `students`: Student information
- `teachers`: Faculty information
- `departments`: Academic departments
- `courses`: Course catalog
- `enrollments`: Student-course relationships
- `timetable`: Class schedules
- `attendance`: Attendance records
- `library`: Book catalog and borrowing
- `fees`: Financial records
- `announcements`: System announcements
- `reports`: Generated reports

## API Documentation

### DAO Classes
All DAO classes follow consistent patterns:
- `add<Entity>()`: Insert new record
- `getAll<Entity>s()`: Retrieve all records
- `findById()`: Find by primary key
- `findBy<UniqueField>()`: Find by unique field
- `update<Entity>()`: Update existing record
- `delete<Entity>()`: Delete record

### Service Classes
- `AuthService`: Authentication and authorization
- `StudentService`: Student-specific business logic
- `TeacherService`: Faculty-specific operations

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check PostgreSQL is running
   - Verify connection parameters in configuration
   - Ensure database exists and user has permissions

2. **Application Won't Start**
   - Check Java version (JDK 11+ required)
   - Verify all JAR files are in lib/ directory
   - Check configuration files exist

3. **Login Issues**
   - Verify username/password
   - Check user role and permissions
   - Ensure account is not locked

4. **UI Display Issues**
   - Check system DPI settings
   - Ensure Java Swing is properly installed
   - Try different look and feel

### Logs
Application logs are written to console. For debugging:
```bash
java -cp "bin:lib/*" -Djava.util.logging.level=FINE main.app.MainApp
```

## Development

### Adding New Features
1. Create model class in `model/`
2. Implement DAO class in `dao/`
3. Add service methods if needed
4. Create UI frame in `ui/`
5. Add to main dashboard navigation
6. Write comprehensive tests

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Handle exceptions appropriately
- Write unit tests for new functionality

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Check the troubleshooting section
- Review the test cases for usage examples
- Examine the DAO classes for data access patterns

## Future Enhancements

- REST API for mobile app integration
- Advanced reporting with charts and graphs
- Email notifications for important events
- Integration with learning management systems
- Mobile application companion
- Advanced analytics and insights
- Multi-language support
- Cloud deployment support