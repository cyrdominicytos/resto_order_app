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

describe('OrdersProductItem e2e test', () => {
  const ordersProductItemPageUrl = '/orders-product-item';
  const ordersProductItemPageUrlPattern = new RegExp('/orders-product-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ordersProductItemSample = {};

  let ordersProductItem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/orders-product-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders-product-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders-product-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ordersProductItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders-product-items/${ordersProductItem.id}`,
      }).then(() => {
        ordersProductItem = undefined;
      });
    }
  });

  it('OrdersProductItems menu should load OrdersProductItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('orders-product-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrdersProductItem').should('exist');
    cy.url().should('match', ordersProductItemPageUrlPattern);
  });

  describe('OrdersProductItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ordersProductItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrdersProductItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/orders-product-item/new$'));
        cy.getEntityCreateUpdateHeading('OrdersProductItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersProductItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders-product-items',
          body: ordersProductItemSample,
        }).then(({ body }) => {
          ordersProductItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders-product-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders-product-items?page=0&size=20>; rel="last",<http://localhost/api/orders-product-items?page=0&size=20>; rel="first"',
              },
              body: [ordersProductItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ordersProductItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrdersProductItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ordersProductItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersProductItemPageUrlPattern);
      });

      it('edit button click should load edit OrdersProductItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrdersProductItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersProductItemPageUrlPattern);
      });

      it('edit button click should load edit OrdersProductItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrdersProductItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersProductItemPageUrlPattern);
      });

      it('last delete button click should delete instance of OrdersProductItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ordersProductItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersProductItemPageUrlPattern);

        ordersProductItem = undefined;
      });
    });
  });

  describe('new OrdersProductItem page', () => {
    beforeEach(() => {
      cy.visit(`${ordersProductItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrdersProductItem');
    });

    it('should create an instance of OrdersProductItem', () => {
      cy.get(`[data-cy="quantity"]`).type('99847').should('have.value', '99847');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ordersProductItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ordersProductItemPageUrlPattern);
    });
  });
});
