- Since when adding @Column, the default name will be "". Adding the name is necessary when using it
- [Entities/Tables must all have an empty constructor](https://stackoverflow.com/questions/25930949/deployment-of-persistenceunit-failed-close-all-factories-for-this-persist), or use @AllArgsConstructor and @NoArgConructor

# About keys
- https://www.baeldung.com/jpa-composite-primary-keys 
- https://stackoverflow.com/a/212371

# About locks
What are the advantages of read-write lock?
- An R/W lock allows concurrent access for read-only operations, whereas write operations require exclusive access. 
- This means that multiple threads can read the data in parallel but an exclusive lock is needed for writing or modifying data.

# Functions and procedures
- createStoredProcedureQuery() is for functions (apparently)
- createNativeQuery is for procedures (apperently)

## Repositories and Mappers
In JPA (Java Persistence API) and Hibernate, the Repository and Mapper interfaces are commonly used to manage database state and handle data access operations. Here's an explanation of each concept:

### Repository Interface:
- The Repository interface acts as an abstraction layer between the application and the underlying database. It provides a set of methods to perform CRUD (Create, Read, Update, Delete) operations on the entities within the database. The Repository interface typically defines methods for querying and manipulating data, such as saving entities, retrieving entities by ID, deleting entities, and executing custom queries.

For example, let's say we have an entity class called User. The UserRepository interface would provide methods like save(User user), findById(Long id), delete(User user), and so on. The actual implementation of the repository interface is typically done using JPA annotations or XML mappings to define the mapping between the entities and the database tables.

At the time, I couldn't understand the difference between mappers and repositories, I literally saw no difference. So we're just using the mappers

### Mapper Interface:
- The Mapper interface, also known as the Data Mapper or Object-Relational Mapping (ORM) layer, is responsible for mapping between the application domain objects/entities and the database tables. It provides methods to convert the data between the object-oriented representation used in the application and the relational representation used in the database.

The Mapper interface defines methods for creating and populating entity objects from the database, as well as methods for persisting entity objects back to the database. It encapsulates the logic for retrieving and storing data in the database, shielding the application code from the underlying database details.

For example, consider the User entity class again. The UserMapper interface would provide methods like mapToEntity(UserDto userDto), mapToDto(User user), mapToEntityList(List<UserDto> userDtos), and so on. These methods handle the conversion between User objects and their corresponding database representations.

The actual implementation of the Mapper interface can be done using various techniques, such as manual mapping using setters and getters, mapping libraries like MapStruct or ModelMapper, or custom mapping logic.

Both the Repository and Mapper interfaces play important roles in database state management and provide a layer of abstraction to simplify the interaction between the application and the database. They promote separation of concerns and make the code more maintainable and testable by decoupling the business logic from the persistence details.