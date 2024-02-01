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

describe('OrderProductItem e2e test', () => {
  const orderProductItemPageUrl = '/order-product-item';
  const orderProductItemPageUrlPattern = new RegExp('/order-product-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const orderProductItemSample = {};

  let orderProductItem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-product-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-product-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-product-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderProductItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-product-items/${orderProductItem.id}`,
      }).then(() => {
        orderProductItem = undefined;
      });
    }
  });

  it('OrderProductItems menu should load OrderProductItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-product-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderProductItem').should('exist');
    cy.url().should('match', orderProductItemPageUrlPattern);
  });

  describe('OrderProductItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderProductItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderProductItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/order-product-item/new$'));
        cy.getEntityCreateUpdateHeading('OrderProductItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderProductItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-product-items',
          body: orderProductItemSample,
        }).then(({ body }) => {
          orderProductItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-product-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-product-items?page=0&size=20>; rel="last",<http://localhost/api/order-product-items?page=0&size=20>; rel="first"',
              },
              body: [orderProductItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderProductItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderProductItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderProductItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderProductItemPageUrlPattern);
      });

      it('edit button click should load edit OrderProductItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderProductItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderProductItemPageUrlPattern);
      });

      it('edit button click should load edit OrderProductItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderProductItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderProductItemPageUrlPattern);
      });

      it('last delete button click should delete instance of OrderProductItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderProductItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderProductItemPageUrlPattern);

        orderProductItem = undefined;
      });
    });
  });

  describe('new OrderProductItem page', () => {
    beforeEach(() => {
      cy.visit(`${orderProductItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderProductItem');
    });

    it('should create an instance of OrderProductItem', () => {
      cy.get(`[data-cy="quantity"]`).type('35914').should('have.value', '35914');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        orderProductItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', orderProductItemPageUrlPattern);
    });
  });
});
