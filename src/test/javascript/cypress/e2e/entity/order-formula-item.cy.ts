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

describe('OrderFormulaItem e2e test', () => {
  const orderFormulaItemPageUrl = '/order-formula-item';
  const orderFormulaItemPageUrlPattern = new RegExp('/order-formula-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const orderFormulaItemSample = {};

  let orderFormulaItem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-formula-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-formula-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-formula-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderFormulaItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-formula-items/${orderFormulaItem.id}`,
      }).then(() => {
        orderFormulaItem = undefined;
      });
    }
  });

  it('OrderFormulaItems menu should load OrderFormulaItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-formula-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderFormulaItem').should('exist');
    cy.url().should('match', orderFormulaItemPageUrlPattern);
  });

  describe('OrderFormulaItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderFormulaItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderFormulaItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/order-formula-item/new$'));
        cy.getEntityCreateUpdateHeading('OrderFormulaItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderFormulaItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-formula-items',
          body: orderFormulaItemSample,
        }).then(({ body }) => {
          orderFormulaItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-formula-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-formula-items?page=0&size=20>; rel="last",<http://localhost/api/order-formula-items?page=0&size=20>; rel="first"',
              },
              body: [orderFormulaItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderFormulaItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderFormulaItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderFormulaItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderFormulaItemPageUrlPattern);
      });

      it('edit button click should load edit OrderFormulaItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderFormulaItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderFormulaItemPageUrlPattern);
      });

      it('edit button click should load edit OrderFormulaItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderFormulaItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderFormulaItemPageUrlPattern);
      });

      it('last delete button click should delete instance of OrderFormulaItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderFormulaItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderFormulaItemPageUrlPattern);

        orderFormulaItem = undefined;
      });
    });
  });

  describe('new OrderFormulaItem page', () => {
    beforeEach(() => {
      cy.visit(`${orderFormulaItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderFormulaItem');
    });

    it('should create an instance of OrderFormulaItem', () => {
      cy.get(`[data-cy="quantity"]`).type('51032').should('have.value', '51032');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        orderFormulaItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', orderFormulaItemPageUrlPattern);
    });
  });
});
