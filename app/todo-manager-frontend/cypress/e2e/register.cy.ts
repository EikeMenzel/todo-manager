describe('Register Component', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/register');
  });

  it('Register: should show invalid inputs error', () => {
    cy.get('#register-button').click();
    cy.contains('Please enter a valid email').should("be.visible")
    cy.contains('Name is required').should("be.visible")
    cy.contains('Surname is required').should("be.visible")
    cy.contains('Password is required').should("be.visible")
  })


  it('Register: should show error for invalid E-Mail', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Your Email is invalid"
      }
    }).as("invalidEmail")

    cy.get('#email').type("iAmNotValid")
    cy.get('#first-name').type("Test User")
    cy.get('#surname').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Your Email is invalid').should("be.visible")
  });

  it('Register: should show error for already used E-Mail', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 409
    }).as("usedEmail")

    cy.get('#email').type("already@used.com")
    cy.get('#first-name').type("Test")
    cy.get('#surname').type("User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Email is already in use. Please choose another one').should("be.visible")
  });

  it('Register: should show error for too short name', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Username is too long or too short"
      }
    }).as("invalidUsername")

    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("T")
    cy.get('#surname').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Your name is too short').should("be.visible")
  });

  it('Register: should show error for too long name', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Username is too long or too short"
      }
    }).as("invalidUsername")

    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("yJmAc0KBANLDKzfFry7M6RZPU3vA8Xrnz0FWfTYiLHaHZHmMzHKkCKa39RMWf77Bz")
    cy.get('#surname').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Your name is too long').should("be.visible")
  });

  it('Register: should show error for too short surname', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Username is too long or too short"
      }
    }).as("invalidUsername")

    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("Test User")
    cy.get('#surname').type("T")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Your surname is too short').should("be.visible")
  });

  it('Register: should show error for too long surname', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Username is too long or too short"
      }
    }).as("invalidUsername")

    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("Test User")
    cy.get('#surname').type("yJmAc0KBANLDKzfFry7M6RZPU3vA8Xrnz0FWfTYiLHaHZHmMzHKkCKa39RMWf77Bz")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Your surname is too long').should("be.visible")
  });


  it('Register: should show error when password are not equal', () => {
    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("Test")
    cy.get('#surname').type("User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser1")

    cy.get('#register-button').click();

    cy.contains("Passwords are not the same").should("be.visible")
  });

  it('Register: should show error for too short Password', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Password is not safe"
      }
    }).as("invalidPassword")


    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("Test")
    cy.get('#surname').type("User")
    cy.get('#password').type("testme")
    cy.get('#confirm-password').type("testme")

    cy.get('#register-button').click();

    cy.contains("Your password is too short")
      .should("be.visible")
  });

  it('Register: should show error for too long Password', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        message: "Password is not safe"
      }
    }).as("invalidPassword")


    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("Test")
    cy.get('#surname').type("User")
    cy.get('#password').type("ThisIsMyNewPasswordThatIsSuperLongSoNoOneCanHackMyAccountThisIsReallySafe")
    cy.get('#confirm-password').type("ThisIsMyNewPasswordThatIsSuperLongSoNoOneCanHackMyAccountThisIsReallySafe")

    cy.get('#register-button').click();

    cy.contains("Your password is too long")
      .should("be.visible")
  });



  it('Register: should register successfully ', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 201
    }).as("validRegistration")
    cy.get('#email').type("test@test.com")
    cy.get('#first-name').type("Test User")
    cy.get('#surname').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();
    cy.contains("Your registration was successful").should("be.visible")
    cy.contains("Back to Login").click().url().should('include',"/login")
  });

  it('Register: should go back to Login ', () => {
    cy.contains("Sign in").click().url().should("include",'/login')
  });

  it('Register: should show server not reachable ', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 500,
    }).as("internalError")

    cy.get('#email').type("testMe@testMe.com")
    cy.get('#first-name').type("Test User")
    cy.get('#surname').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains("Server cannot be reached").should("be.visible")
  });

  it('Register: should show error for no authorization', () => {
    cy.intercept('POST', 'api/v1/register', {
      statusCode: 412
    }).as("preconditionFailed")

    cy.get('#email').type("testit@testit.com")
    cy.get('#first-name').type("User")
    cy.get('#surname').type("Test")
    cy.get('#password').type("TestUser!1")
    cy.get('#confirm-password').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Authorizing your request failed').should("be.visible")

  });

})
