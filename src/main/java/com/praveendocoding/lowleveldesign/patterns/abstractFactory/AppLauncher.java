package com.praveendocoding.lowleveldesign.patterns.abstractFactory;


import java.awt.*;

interface Button {
    void onClick();
    void paint();
}

interface Checkbox {
    void onSelect();
    void paint();
}

class WindowButton implements Button {
    @Override
    public void onClick() {
        System.out.println("Windows button clicked");
    }

    @Override
    public void paint() {
        System.out.println("Painting a Windows-Styled button");
    }
}

class WindowCheckbox implements Checkbox {
    @Override
    public void onSelect() {
        System.out.println("Windows checkbox selected");
    }
    @Override
    public void paint() {
        System.out.println("Painting a Windows-Styled checkbox");
    }
}

class MacButton implements Button {
    @Override
    public void onClick() {
        System.out.println("Mac button clicked");
    }

    @Override
    public void paint() {
        System.out.println("Painting a Mac-Styled button");
    }
}

class MacCheckbox implements Checkbox {
    @Override
    public void onSelect() {
        System.out.println("Mac checkbox selected");
    }
    @Override
    public void paint() {
        System.out.println("Painting a Mac-Styled checkbox");
    }
}


interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}


class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowButton();
    }
    @Override
    public Checkbox createCheckbox() {
        return new WindowCheckbox();
    }
}

class MacFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }
    @Override
    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}

class Application {
    private final Button button;
    private final Checkbox checkbox;

    public Application(GUIFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
    }

    public void renderUI() {
        button.paint();
        checkbox.paint();
    }
}


public class AppLauncher {
    public static void main( String[] args){
        String os = System.getProperty("os.name");
        GUIFactory factory;

        if(os.equals("Windows")){
            factory = new WindowsFactory();
        }
        else if(os.equals("Mac OS X")){
            factory = new MacFactory();
        }else{
            System.out.println("Unknown operating system: " + os);
            return;
        }

        Application app = new Application(factory);

        app.renderUI();

    }
}
