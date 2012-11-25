package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.component.DataTableComponent;
import org.lightadmin.component.FilterFormComponent;
import org.lightadmin.data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.Assert.fail;

public class ListViewPage extends SecuredPage<ListViewPage> {

	public static final String DEFAULT_SCOPE_LABEL = "All";
	private Domain domain;

	@FindBy( id = "listViewTable" )
	private WebElement listViewTable;

    private WebElement scope;

    private FilterFormComponent filterFormComponent;

    public ListViewPage( SeleniumContext seleniumContext, Domain domain  ) {
		super( seleniumContext );

        filterFormComponent = new FilterFormComponent( seleniumContext );
		this.domain = domain;
	}

	public DataTableComponent getDataTable() {
		return new DataTableComponent( listViewTable );
	}

	@Override
	protected void load() {
		webDriver().get( baseUrl().toString() + "/domain/" + domain.getDomainTypeName() );
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			waitForElementVisible( listViewTable );
		} catch ( TimeoutException e) {
			fail( "ListViewTable was not found on ListViewPage" );
		}

		try {
			waitForElementVisible( getScope( DEFAULT_SCOPE_LABEL ) );
		} catch ( TimeoutException e) {
			fail( "Default scope is not displayed" );
		}
	}

    public void selectScope( String scopeLabel ) {
        getScope( scopeLabel ).click();
    }

    public boolean scopeIsHighlighted( String scopeLabel ) {
        try {
            return getScope( scopeLabel ).getAttribute( "class" ).contains( "label-success" );
        } catch ( NoSuchElementException e ) {
            return false;
        }
    }

    public void resetFilter() {
        filterFormComponent.resetFilter();
    }

    public void filter(String filterField, String filterValue) {
        filterFormComponent.filter( filterField, filterValue );
    }

    private WebElement getScope( String scopeLabel ) {
        return webDriver().findElement( By.linkText( scopeLabel ) );
    }
}