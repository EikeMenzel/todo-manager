describe('Register Component', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/register');
  });

  it('Register: should show invalid inputs error', () => {
    cy.get('#register-button').click();
    cy.contains('Username is required').should("be.visible")
    cy.contains('Password is required').should("be.visible")
  })

  it('Register: should show invalid inputs error for password only', () => {
    cy.get('#username-input').type("TestUser")
    cy.get('#register-button').click();
    cy.contains('Username is required').should("not.exist")
    cy.contains('Password is required').should("be.visible")
  })

  it('Register: should show invalid inputs error for username only', () => {
    cy.get('#password-input').type("SuperSecretPw1!")
    cy.get('#register-button').click();
    cy.contains('Username is required').should("be.visible")
    cy.contains('Password is required').should("not.exist")
  })

  it('Register: should show login success ', () => {
    cy.intercept('POST', 'api/v1/register', {
      statusCode: 200,
      body: {
        status: 1,
        message: "Your Email is invalid"
      }
    }).as("invalidEmail")
    cy.get('#username-input').type("TestUser")
    cy.get('#password-input').type("SuperSecretPw1!")
    cy.get('#register-button').click();
    cy.contains("Your Account has been created! Please return to the login page to proceed!").should("be.visible")
  });

  it('Register: should show login success try clicking login', () => {
    cy.intercept('POST', 'api/v1/register', {
      statusCode: 200,
      body: {
        status: 1,
        message: "Your Email is invalid"
      }
    }).as("invalidEmail")
    cy.get('#username-input').type("TestUser")
    cy.get('#password-input').type("SuperSecretPw1!")
    cy.get('#register-button').click();
    cy.contains("Your Account has been created! Please return to the login page to proceed!").should("be.visible")

    cy.get('#go-to-login').click().url().should('include',"/login")
  });

  it('Register: click goto login - go to login', () => {
    cy.get('#login-now').click().url().should('include',"/login")
  });
})
