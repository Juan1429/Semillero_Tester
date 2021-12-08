package pages;

import org.openqa.selenium.By;

public class HacebPages {


    By txtBuscador = By.xpath("//input[@placeholder='¿Buscas un producto en especial?']");
    By btnBuscador = By.xpath("//button[@class='btn btn-search']");
    By btnElementoBusqueda;
    By txtElementoBusqueda;


    public By getTxtBuscador() {
        return txtBuscador;
    }

    public By getBtnBuscador() {
        return btnBuscador;
    }

    public By getBtnElementoBusqueda() {
        return btnElementoBusqueda;
    }

    public By getTxtElementoBusqueda() {
        return txtElementoBusqueda;
    }

    public void setBtnElementoBusqueda(String producto) {
        this.btnElementoBusqueda = By.xpath("//div[@class='shelve__item' and @data-name='"+producto+"']");
    }


    public void setTxtElementoBusqueda(String producto) {
        this.txtElementoBusqueda = By.xpath("//div[@class='column']//h1[contains(text(),'"+producto+"')]");
    }


}

