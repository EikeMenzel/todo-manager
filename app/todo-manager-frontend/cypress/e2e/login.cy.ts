describe('Login Component', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/login');
  });

  it('Login: should show invalid inputs error for both fields', () => {
    cy.get('#login-button').click();
    cy.contains('Username and Password are required.').should("be.visible")
  })

  it('Login: should show invalid inputs error for password', () => {
    cy.get('#username-input').type("TestUser")
    cy.get('#login-button').click();
    cy.contains('Password is required.').should("be.visible")
  })

  it('Login: should show invalid inputs error for password', () => {
    cy.get('#password-input').type("Password")
    cy.get('#login-button').click();
    cy.contains('Username is required.').should("be.visible")
  })

  it('Login: should show login and redirect', () => {
    cy.intercept('POST', 'api/v1/login', {
      statusCode: 200,
      body: {
        "token": "1345"
      }
    }).as("login-request")
    cy.get('#password-input').type("Password")
    cy.get('#username-input').type("Username")
    cy.contains('Username is required.').should("not.exist")
    cy.contains('Password is required.').should("not.exist")
    cy.contains('Username and Password are required.').should("not.exist")
    cy.get('#login-button').click().url().should('not.include',"/login");
  })

  it('Login: click goto register - go to register', () => {
    cy.get('#register-now').click().url().should('include',"/register")
  });
})
