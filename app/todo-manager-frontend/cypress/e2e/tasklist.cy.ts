describe('Task Component', () => {
  beforeEach(() => {
    window.localStorage.setItem('todo-user', "1")
  });

  it('Task List: Should say no item found', () => {
    cy.visit('http://localhost:4200');
    cy.contains('No tasks yet. Please create a new one').should("be.visible")
    cy.contains('No categories yet. Please create a new one').should("be.visible")
  })

  it('Task List: Add Category results in prompt', () => {
    cy.intercept('POST', 'api/v1/categories', {
      statusCode: 201,
    }).as("addCategory")
    cy.visit('http://localhost:4200');
    cy.intercept('GET', 'api/v1/categories', {
      statusCode: 200,
      body: [
        {
          id: '1',
          name: 'New Category Name'
        }
      ]
    }).as("afterAddCategory")
    cy.window().then((win) => {
      cy.stub(win, 'prompt').returns('New Category Name');
    });

    cy.get("#add-task").should("be.disabled")
    cy.get("#add-category").click()
    cy.get("#add-task").should("be.enabled")
    let cat = cy.contains('New Category Name')
    cat.should("be.visible")
    cat.should("have.class", "active-category")
  })

  it('Task List: Add Task results in modal', () => {
    cy.intercept('GET', 'api/v1/categories', {
      statusCode: 200,
      body: [
        {
          id: '1',
          name: 'New Category Name'
        }
      ]
    }).as("getCategories")
    cy.intercept('POST', '/api/v1/categories/1/tasks', {
      statusCode: 200,
    }).as("addTask")
    cy.visit('http://localhost:4200');
    cy.get("#add-task").should("be.enabled")
    cy.get("#add-task").click()
    cy.contains('Add new task').should("be.visible")
    cy.get("#save-task-button").should("be.disabled")
    cy.get("#task-text-input").type("Dummy description")
    cy.get("#save-task-button").should("be.enabled")
    cy.get("#delete-task-button").should("not.exist")

    cy.intercept('GET', 'api/v1/categories/1/tasks', {
      statusCode: 200,
      body: [
        {
          id: '1',
          text: 'Dummy description',
          priority: "LOW",
          status: "NOT_STARTED",
          categoryId: 1
        }
      ]
    }).as("getTasks")
    cy.get("#save-task-button").click()
    cy.get("#save-task-button").should("not.exist")
    cy.contains("Dummy description").should("be.visible")
    cy.contains("Not Started").should("be.visible")
  })

  it('Task List: Update Task', () => {
    cy.intercept('GET', 'api/v1/categories', {
      statusCode: 200,
      body: [
        {
          id: '1',
          name: 'New Category Name'
        }
      ]
    }).as("getCategories")
    cy.intercept('GET', 'api/v1/categories/1/tasks', {
      statusCode: 200,
      body: [
        {
          id: '1',
          text: 'Dummy description',
          priority: "LOW",
          status: "NOT_STARTED",
          categoryId: 1
        }
      ]
    }).as("getTasks")
    cy.visit('http://localhost:4200');
    cy.contains("Dummy description").click()
    cy.get("#save-task-button").should("be.visible")
    cy.get("#task-text-input").should('have.value', 'Dummy description');
    cy.get("#delete-task-button").should("be.visible")
    cy.get("#task-text-input").clear()
    cy.get("#save-task-button").should("be.disabled")
    cy.get("#task-text-input").type("New Task description")
    cy.get("#save-task-button").should("be.visible")
    cy.intercept('GET', 'api/v1/categories/1/tasks', {
      statusCode: 200,
      body: [
        {
          id: '1',
          text: 'New Task description',
          priority: "LOW",
          status: "NOT_STARTED",
          categoryId: 1
        }
      ]
    }).as("getTasks")
    cy.intercept('PUT', 'api/v1/categories/1/tasks/1', {
      statusCode: 200,
    }).as("updateTaskDescription")
    cy.get("#save-task-button").click()
    cy.contains("New Task description").should("be.visible")
  })
  it('Task List: Delete Task', () => {
    cy.intercept('GET', 'api/v1/categories', {
      statusCode: 200,
      body: [
        {
          id: '1',
          name: 'New Category Name'
        }
      ]
    }).as("getCategories")
    cy.intercept('GET', 'api/v1/categories/1/tasks', {
      statusCode: 200,
      body: [
        {
          id: '1',
          text: 'Dummy description',
          priority: "LOW",
          status: "NOT_STARTED",
          categoryId: 1
        }
      ]
    }).as("getTasks")
    cy.visit('http://localhost:4200');
    cy.contains("Dummy description").click()
    cy.get("#save-task-button").should("be.visible")
    cy.get("#delete-task-button").should("be.visible")
    cy.intercept('DELETE', 'api/v1/categories/1/tasks/1', {
      statusCode: 200,
    }).as("deleteTaskDescription")
    cy.intercept('GET', 'api/v1/categories/1/tasks', {
      statusCode: 200,
      body: []
    }).as("getTasks")
    cy.get("#delete-task-button").click()
    cy.contains("No tasks yet. Please create a new one").should("be.visible")
  })
})
