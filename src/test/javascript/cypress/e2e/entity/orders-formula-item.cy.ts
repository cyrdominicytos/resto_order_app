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

describe('OrdersFormulaItem e2e test', () => {
  const ordersFormulaItemPageUrl = '/orders-formula-item';
  const ordersFormulaItemPageUrlPattern = new RegExp('/orders-formula-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ordersFormulaItemSample = {};

  let ordersFormulaItem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/orders-formula-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders-formula-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders-formula-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ordersFormulaItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders-formula-items/${ordersFormulaItem.id}`,
      }).then(() => {
        ordersFormulaItem = undefined;
      });
    }
  });

  it('OrdersFormulaItems menu should load OrdersFormulaItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('orders-formula-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrdersFormulaItem').should('exist');
    cy.url().should('match', ordersFormulaItemPageUrlPattern);
  });

  describe('OrdersFormulaItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ordersFormulaItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrdersFormulaItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/orders-formula-item/new$'));
        cy.getEntityCreateUpdateHeading('OrdersFormulaItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersFormulaItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders-formula-items',
          body: ordersFormulaItemSample,
        }).then(({ body }) => {
          ordersFormulaItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders-formula-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders-formula-items?page=0&size=20>; rel="last",<http://localhost/api/orders-formula-items?page=0&size=20>; rel="first"',
              },
              body: [ordersFormulaItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ordersFormulaItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrdersFormulaItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ordersFormulaItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersFormulaItemPageUrlPattern);
      });

      it('edit button click should load edit OrdersFormulaItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrdersFormulaItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersFormulaItemPageUrlPattern);
      });

      it('edit button click should load edit OrdersFormulaItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrdersFormulaItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersFormulaItemPageUrlPattern);
      });

      it('last delete button click should delete instance of OrdersFormulaItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ordersFormulaItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersFormulaItemPageUrlPattern);

        ordersFormulaItem = undefined;
      });
    });
  });

  describe('new OrdersFormulaItem page', () => {
    beforeEach(() => {
      cy.visit(`${ordersFormulaItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrdersFormulaItem');
    });

    it('should create an instance of OrdersFormulaItem', () => {
      cy.get(`[data-cy="quantity"]`).type('1293').should('have.value', '1293');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ordersFormulaItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ordersFormulaItemPageUrlPattern);
    });
  });
});
