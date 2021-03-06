### Coding Assignment

#### Project Requirements
* Gradle Version 6+
* Upon completion, check-in the code into a public git repo and provide the link.

#### Assignment Technology Requirements
* Java
* Gradle
* TestNG or JUnit 5
* Any libraries or frameworks you consider necessary

#### Expectations
* We are interested in the naming conventions, readability, structure and modularity of your implementation. 
* Variety and quality of the test cases.
* Imagine that this small example is going to turn into a larger framework.

#### Assumptions
* The database (hsqldb) has been included and is located in the `db` folder
* Database connection configuration can be found [here](settings.gradle)
* The database has four tables: `CUSTOMER`, `INVOICE`, `ITEM` and `PRODUCT` with ~ 50 rows each
* Two Gradle tasks, `startDB` and `stopDB` have been provided for starting and stopping the database. <br>
  Examples:<br> 
    - Start the database: `gradle startDB`
    - Stop the database: `gradle stopDB`
    - Start the database, execute tests and then stop the database: `gradle clean startDB test stopDB`
    
* Command for the Database Manager `java -cp db/hsqldb/lib/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing`

#### Exercises
##### <u>Exercise #1</u> <br>
`Write a query that finds the customer's id, first name, last name with the item's invoice id, item number and quantity along with the product name for the invoice id of 10.` <br>
`Then write validation that the id is 36, customer's name is Janet King with 5 rows of products:` <br>
`Clock Clock with a Quantity of 24`<br>
`Shoe Iron with a Quantity of 8`<br>
`Telephone Chair with a Quantity of 4` <br>
`Iron Telephone with a Quantity of 22` <br>
`Shoe Ice Tea with a Quantity of 4` <br>

##### <u>Exercise #2</u> <br>
`Write an insert statement into the INVOICE table for customer Susan Smith using the following products:`<br>
`Iron Clock ($6.60)` <br>
`Iron Telephone ($23.60)` <br> 
`Shoe Shoe ($5.20) ` <br>
`Then write a query to validate the inserted data`

##### <u>Exercise #3</u>

##### Part 1: 

`At the end of the README file, document the test cases you think are necessary to validate the totalAmountOfInvoicesById method in the [com/rr19/example/application/InvoicePriceCalculator] class. (Note: This class should NOT be modify or changed in any fashion.`   <br>
`In addition, don't hesitate to add any notes, observations, concerns or suggestions`

##### Part 2: 

`Code the majority of the documented test cases` 


<hr>

#### Test Cases:

`Found in invoicePriceCalculatorTest.java`
`1. Test 10% of current customerId invoices with existing records`
`2. Test sum with additional invoice record`
`3. Test sum on user with no invoices`


#### Notes
`Exercise 1 is in UtilMethodTests.java line 150 and Tested on Line 47`
`Exercise 2 is in InvoiceUtils.java line 13, , tests and data prep in UtilMethodTests.java line 74`
`Exercise 3 part 2 can be found in invoicPriceCalculator test class`

#### Concerns
`1. Susanne Smith vs Susan Smith`
`2. IDENTITY column is not set for the purposes of insert. This leaves us calculating MAX or sorting to determine the last inserted Id which can lead to RACE conditions when multiple users grab MAX for puproses of inserting.`

#### Suggestions
`1. Implemented connection pooling strategy and singleton datasource for TestClasses with need to query`
`2. Implemented reusable DAO objects for Customer, Invoice, Item, Product table selection and inserts`
`3. Implemented validation on DAO and Util methods`
`4. I wasn't able to modify invoicePriceCalculator test, but i have done some examples where passing the connection parameter in allowed me to insert, test for the condition and exit within the same test leading to a transaction rollback.  I think this is more efficient than inserting and deleting a row per each test`