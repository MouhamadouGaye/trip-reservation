src/
├── main/
│   ├── java/com/tripreserve/
│   │   ├── TripReserveApplication.java
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── config/
│   └── resources/
│       └── application.properties


Completed Backend Components
1. Core Application
Main Spring Boot application class with proper annotations (@SpringBootApplication, @EnableScheduling, @EnableTransactionManagement)

2. Entity Classes
User (implements UserDetails for Spring Security integration)

Trip (with dynamic pricing logic)

Booking (with seat management)

Enums: VehicleType, Amenity, BookingStatus

3. DTOs (Data Transfer Objects)
UserRegistrationDto - User registration data

LoginDto - Authentication credentials

TripSearchDto - Trip search criteria

BookingCreateDto - Booking creation data

TripResponseDto - Trip response with dynamic pricing

4. Repositories
UserRepository - User data access

TripRepository - Trip data access with custom queries

BookingRepository - Booking data access with seat management

5. Services ✅ (Fixed)
UserService (implements UserDetailsService) - User management and authentication

TripService - Trip search and dynamic pricing calculation

BookingService - Booking management and seat allocation

JwtService - Complete JWT token generation and validation

6. Controllers
AuthController - User registration and login endpoints

TripController - Trip search and location endpoints

BookingController - Booking management endpoints

UserController - User profile management

7. Security Configuration ✅ (Fixed)
JWT-based authentication with bearer tokens

CORS configuration for frontend integration

Password encoding using BCrypt

Proper authentication provider setup with DaoAuthenticationProvider

8. Additional Components
JwtAuthenticationFilter - Processes JWT tokens in requests

GlobalExceptionHandler - Handles validation errors and exceptions

DataInitializer - Populates sample trip data

Application properties configuration - Database and JWT settings

🚀 API Endpoints Available
Authentication
POST /api/auth/register - User registration

POST /api/auth/login - User login (returns JWT)

Trips (Public)
POST /api/trips/search - Search for trips with filters

GET /api/trips/origins - Get all origin cities

GET /api/trips/destinations - Get all destination cities

Bookings (Protected - Requires JWT)
POST /api/bookings - Create a new booking

GET /api/bookings - Get user's bookings

GET /api/bookings/{referenceNumber} - Get booking by reference number

DELETE /api/bookings/{id} - Cancel a booking

User (Protected - Requires JWT)
GET /api/user/profile - Get user profile

PUT /api/user/profile - Update user profile

🔐 Security Features
JWT token-based authentication with expiration

Password encryption using BCrypt hashing

Role-based access control for protected endpoints

CORS enabled for frontend integration (localhost:3000)

Input validation with proper error messages

📊 Database
H2 in-memory database for development and testing

Sample data automatically populated with sample trips

H2 console available at /h2-console for database inspection

Automatic schema generation with Hibernate

🎯 Key Business Features
Dynamic pricing based on demand, timing, and market conditions

Seat availability checking with real-time validation

Flexible date searching (±3 days from requested date)

Booking reference number generation with UUID-based unique codes

Email/phone validation with regex patterns

Special requests and newsletter subscriptions support

Weekend and last-minute pricing premiums

Discount calculation for promotional pricing

🚀 Getting Started
Prerequisites
Java 17+

Maven 3.6+

Spring Boot 3.0+

Installation & Running
Clone the repository

Run mvn clean install

Start with mvn spring-boot:run

Application will start on http://localhost:8080

Testing Workflow
Register a user: POST /api/auth/register

Login to get JWT token: POST /api/auth/login

Use the token: Include in Authorization header: Bearer {your-token}

Search for trips: POST /api/trips/search

Make bookings: POST /api/bookings