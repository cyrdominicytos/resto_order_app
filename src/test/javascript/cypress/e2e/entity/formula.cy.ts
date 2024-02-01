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

describe('Formula e2e test', () => {
  const formulaPageUrl = '/formula';
  const formulaPageUrlPattern = new RegExp('/formula(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formulaSample = { price: 15547 };

  let formula;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/formulas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/formulas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/formulas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formula) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/formulas/${formula.id}`,
      }).then(() => {
        formula = undefined;
      });
    }
  });

  it('Formulas menu should load Formulas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('formula');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Formula').should('exist');
    cy.url().should('match', formulaPageUrlPattern);
  });

  describe('Formula page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formulaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Formula page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/formula/new$'));
        cy.getEntityCreateUpdateHeading('Formula');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formulaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/formulas',
          body: formulaSample,
        }).then(({ body }) => {
          formula = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/formulas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/formulas?page=0&size=20>; rel="last",<http://localhost/api/formulas?page=0&size=20>; rel="first"',
              },
              body: [formula],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(formulaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Formula page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formula');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formulaPageUrlPattern);
      });

      it('edit button click should load edit Formula page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Formula');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formulaPageUrlPattern);
      });

      it('edit button click should load edit Formula page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Formula');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formulaPageUrlPattern);
      });

      it('last delete button click should delete instance of Formula', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formula').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formulaPageUrlPattern);

        formula = undefined;
      });
    });
  });

  describe('new Formula page', () => {
    beforeEach(() => {
      cy.visit(`${formulaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Formula');
    });

    it('should create an instance of Formula', () => {
      cy.get(`[data-cy="price"]`).type('20927').should('have.value', '20927');

      cy.get(`[data-cy="name"]`).type('Refined').should('have.value', 'Refined');

      cy.setFieldImageAsBytesOfEntity('photo', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="imageType"]`).type('Arkansas Gold reinvent').should('have.value', 'Arkansas Gold reinvent');

      cy.get(`[data-cy="description"]`).type('Towels withdrawal').should('have.value', 'Towels withdrawal');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-01T01:58').blur().should('have.value', '2024-02-01T01:58');

      cy.get(`[data-cy="updatedDate"]`).type('2024-01-31T09:33').blur().should('have.value', '2024-01-31T09:33');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formula = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formulaPageUrlPattern);
    });
  });
});
