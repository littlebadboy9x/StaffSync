### Employee Management System

A comprehensive Spring Boot application for managing employees, departments, facilities, and specializations. This system allows organizations to track employee information, manage their specializations across different facilities, and handle data import/export operations.

## Technologies Used

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **Thymeleaf** for server-side templating
- **Bootstrap 5** for responsive UI
- **jQuery** for AJAX operations
- **Microsoft SQL Server** for database
- **Apache POI** for Excel file operations
- **Lombok** for reducing boilerplate code
- **ModelMapper** for DTO conversions


## Database Schema

The application uses the following database schema:

```mermaid
Database Schema.download-icon {
            cursor: pointer;
            transform-origin: center;
        }
        .download-icon .arrow-part {
            transition: transform 0.35s cubic-bezier(0.35, 0.2, 0.14, 0.95);
             transform-origin: center;
        }
        button:has(.download-icon):hover .download-icon .arrow-part, button:has(.download-icon):focus-visible .download-icon .arrow-part {
          transform: translateY(-1.5px);
        }
        #mermaid-diagram-rc13{font-family:var(--font-geist-sans);font-size:12px;fill:#000000;}#mermaid-diagram-rc13 .error-icon{fill:#552222;}#mermaid-diagram-rc13 .error-text{fill:#552222;stroke:#552222;}#mermaid-diagram-rc13 .edge-thickness-normal{stroke-width:1px;}#mermaid-diagram-rc13 .edge-thickness-thick{stroke-width:3.5px;}#mermaid-diagram-rc13 .edge-pattern-solid{stroke-dasharray:0;}#mermaid-diagram-rc13 .edge-thickness-invisible{stroke-width:0;fill:none;}#mermaid-diagram-rc13 .edge-pattern-dashed{stroke-dasharray:3;}#mermaid-diagram-rc13 .edge-pattern-dotted{stroke-dasharray:2;}#mermaid-diagram-rc13 .marker{fill:#666;stroke:#666;}#mermaid-diagram-rc13 .marker.cross{stroke:#666;}#mermaid-diagram-rc13 svg{font-family:var(--font-geist-sans);font-size:12px;}#mermaid-diagram-rc13 p{margin:0;}#mermaid-diagram-rc13 .entityBox{fill:#eee;stroke:#999;}#mermaid-diagram-rc13 .attributeBoxOdd{fill:#ffffff;stroke:#999;}#mermaid-diagram-rc13 .attributeBoxEven{fill:#f2f2f2;stroke:#999;}#mermaid-diagram-rc13 .relationshipLabelBox{fill:hsl(-160, 0%, 93.3333333333%);opacity:0.7;background-color:hsl(-160, 0%, 93.3333333333%);}#mermaid-diagram-rc13 .relationshipLabelBox rect{opacity:0.5;}#mermaid-diagram-rc13 .relationshipLine{stroke:#666;}#mermaid-diagram-rc13 .entityTitleText{text-anchor:middle;font-size:18px;fill:#000000;}#mermaid-diagram-rc13 #MD_PARENT_START{fill:#f5f5f5!important;stroke:#666!important;stroke-width:1;}#mermaid-diagram-rc13 #MD_PARENT_END{fill:#f5f5f5!important;stroke:#666!important;stroke-width:1;}#mermaid-diagram-rc13 .flowchart-link{stroke:hsl(var(--gray-400));stroke-width:1px;}#mermaid-diagram-rc13 .marker,#mermaid-diagram-rc13 marker,#mermaid-diagram-rc13 marker *{fill:hsl(var(--gray-400))!important;stroke:hsl(var(--gray-400))!important;}#mermaid-diagram-rc13 .label,#mermaid-diagram-rc13 text,#mermaid-diagram-rc13 text>tspan{fill:hsl(var(--black))!important;color:hsl(var(--black))!important;}#mermaid-diagram-rc13 .background,#mermaid-diagram-rc13 rect.relationshipLabelBox{fill:hsl(var(--white))!important;}#mermaid-diagram-rc13 .entityBox,#mermaid-diagram-rc13 .attributeBoxEven{fill:hsl(var(--gray-150))!important;}#mermaid-diagram-rc13 .attributeBoxOdd{fill:hsl(var(--white))!important;}#mermaid-diagram-rc13 .label-container,#mermaid-diagram-rc13 rect.actor{fill:hsl(var(--white))!important;stroke:hsl(var(--gray-400))!important;}#mermaid-diagram-rc13 line{stroke:hsl(var(--gray-400))!important;}#mermaid-diagram-rc13 :root{--mermaid-font-family:var(--font-geist-sans);}staffUUIDidPKstringstaff_codestringnamestringaccount_fptstringaccount_fetinyintstatusbigintcreated_datebigintlast_modified_datefacilityUUIDidPKstringcodestringnametinyintstatusbigintcreated_datebigintlast_modified_datedepartmentUUIDidPKstringcodestringnametinyintstatusbigintcreated_datebigintlast_modified_datedepartment_facilityUUIDidPKUUIDid_departmentFKUUIDid_facilityFKUUIDid_staffFKtinyintstatusbigintcreated_datebigintlast_modified_datemajorUUIDidPKstringcodestringnametinyintstatusbigintcreated_datebigintlast_modified_datemajor_facilityUUIDidPKUUIDid_department_facilityFKUUIDid_majorFKtinyintstatusbigintcreated_datebigintlast_modified_datestaff_major_facilityUUIDidPKUUIDid_major_facilityFKUUIDid_staffFKtinyintstatusbigintcreated_datebigintlast_modified_datemanageshascontainsbelongs_tohasbelongs_toassigned_to
```

## Features

### Staff Management

- Create, read, update, and delete staff records
- Track staff information including name, code, and email addresses
- Toggle staff active status


### Facility and Department Management

- Manage facilities (campuses) and departments
- Associate departments with facilities
- Assign department heads


### Specialization Management

- Manage specializations (majors) across departments
- Assign staff to specializations at specific facilities
- Track staff specialization history


### Data Import/Export

- Import staff data from Excel files
- Export staff data to Excel format
- Track import history and errors


### User Interface

- Responsive web interface using Bootstrap
- Interactive forms with client-side validation
- AJAX-based operations for smooth user experience


## Setup and Installation

### Prerequisites

- Java 17 or higher
- Microsoft SQL Server
- Maven


### Database Setup

1. Create a SQL Server database named `exam_distribution_test`
2. Run the SQL script provided in the `database-setup.sql` file to create the necessary tables and sample data


### Application Configuration

1. Clone the repository
2. Configure the database connection in `src/main/resources/application.properties`:

```plaintext
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=exam_distribution_test;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```


3. Build the application:

```shellscript
mvn clean package
```


4. Run the application:

```shellscript
java -jar target/employee-management-0.0.1-SNAPSHOT.jar
```


5. Access the application at `http://localhost:8080`


## API Endpoints

### Staff API

- `GET /api/staff` - Get all staff
- `GET /api/staff/{id}` - Get staff by ID
- `GET /api/staff/code/{code}` - Get staff by code
- `POST /api/staff` - Create new staff
- `PUT /api/staff/{id}` - Update staff
- `PUT /api/staff/{id}/toggle-status` - Toggle staff status


### Specialization API

- `GET /api/specializations/facilities` - Get all facilities
- `GET /api/specializations/departments/{facilityId}` - Get departments by facility
- `GET /api/specializations/majors/{departmentFacilityId}` - Get majors by department facility
- `GET /api/specializations/staff/{staffId}` - Get staff specializations
- `POST /api/specializations/staff/{staffId}` - Add staff specialization
- `DELETE /api/specializations/{id}` - Remove staff specialization


### Excel API

- `GET /api/excel/template` - Download Excel template
- `POST /api/excel/import` - Import staff data
- `GET /api/excel/import-history` - Get import history


## Project Structure

```plaintext
src/main/java/com/fpt/employeemanagement/
├── config/                  # Configuration classes
├── controller/              # REST and view controllers
├── dto/                     # Data Transfer Objects
├── exception/               # Exception handling
├── Entity/                   # Entity classes
├── repository/              # JPA repositories
├── service/                 # Business logic
└── StaffSync.java  # Main application class

src/main/resources/
├── static/                  # Static resources (CSS, JS)
├── templates/               # Thymeleaf templates
└── application.properties   # Application configuration
```

## Screenshots

### Staff List





### Staff Form





### Specialization Management





### Import/Export





## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- Spring Boot and Spring Data JPA for the robust backend framework
- Bootstrap for the responsive UI components
- jQuery for simplifying client-side scripting
- Apache POI for Excel file handling


---

© 2025 FPOLY. All rights reserved.
