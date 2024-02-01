import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Orders e2e test', () => {
  const ordersPageUrl = '/orders';
  const ordersPageUrlPattern = new RegExp('/orders(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ordersSample = { amount: 12989 };

  let orders;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orders) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${orders.id}`,
      }).then(() => {
        orders = undefined;
      });
    }
  });

  it('Orders menu should load Orders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('orders');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Orders').should('exist');
    cy.url().should('match', ordersPageUrlPattern);
  });

  describe('Orders page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ordersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Orders page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/orders/new$'));
        cy.getEntityCreateUpdateHeading('Orders');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders',
          body: ordersSample,
        }).then(({ body }) => {
          orders = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders?page=0&size=20>; rel="last",<http://localhost/api/orders?page=0&size=20>; rel="first"',
              },
              body: [orders],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ordersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Orders page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orders');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });

      it('edit button click should load edit Orders page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Orders');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });

      it('edit button click should load edit Orders page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Orders');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });

      it('last delete button click should delete instance of Orders', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orders').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);

        orders = undefined;
      });
    });
  });

  describe('new Orders page', () => {
    beforeEach(() => {
      cy.visit(`${ordersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Orders');
    });

    it('should create an instance of Orders', () => {
      cy.get(`[data-cy="amount"]`).type('60789').should('have.value', '60789');

      cy.get(`[data-cy="createdDate"]`).type('2024-01-31T15:00').blur().should('have.value', '2024-01-31T15:00');

      cy.get(`[data-cy="recoveryDate"]`).type('2024-02-01T05:16').blur().should('have.value', '2024-02-01T05:16');

      cy.get(`[data-cy="updatedDate"]`).type('2024-01-31T15:04').blur().should('have.value', '2024-01-31T15:04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        orders = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ordersPageUrlPattern);
    });
  });
});