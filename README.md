Deliverable 1: Build System Instructions

------------------------------------------------------------------------------------------------------------------------------------------------------------------------
* As per deliverable 1 instructions and confirmation by TAs, this README file is to contain instructions regarding the required configs aspect of the build system.
* The other part of the build system submission (Testing Summary) is to be completed as part of the wiki along with the project report.
------------------------------------------------------------------------------------------------------------------------------------------------------------------------

STEP 1: Clone The Repository
--------
Begin by opening your terminal, using the cd command to navigate your local directories, and creating a local clone of our git repository in a location of your choosing using the following command:
git clone https://github.com/McGill-ECSE321-Fall2024/project-group-10.git

Once this is done, you will have a folder called project-group-10 on your machine, containing our codebase under the GameShop folder.


STEP 2: Set up the Local PostgreSQL Database
--------

With the repository now locally, cloned, you must create a local PostgreSQL database on your machine, 
following the detailed instructions found here: https://mcgill-ecse321-fall2024.github.io/tutorial-notes/#_setting_up_a_local_postgresql_database

You are to follow these instructions with the following information specific to our database:
- NAME: gameshop
- PORT NUMBER: 5432
- PASSWORD: grouptenpsql


Quick Summary of PostgreSQL Setup:
1. Start PostgreSQL server.
2. Open psql.
Step 3: Create a database with the name gameshop.
Step 4: Set the port to 5432 and ensure you have the correct password (grouptenpsql).

STEP 3: Launch IDE & Run Tests
--------
Now, with the repository and PostgreSQL database set up locally on your machine, you may build and run the tests.
Launch your IDE and connect your IDE to the database.
Then, run the following command to build and test: ./gradlew build -xtest
