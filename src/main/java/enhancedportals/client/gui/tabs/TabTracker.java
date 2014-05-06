package enhancedportals.client.gui.tabs;

public class TabTracker
{
    private static Class<? extends BaseTab> openedLeftTab;
    private static Class<? extends BaseTab> openedRightTab;

    public static Class<? extends BaseTab> getOpenedLeftTab()
    {
        return openedLeftTab;
    }

    public static Class<? extends BaseTab> getOpenedRightTab()
    {
        return openedRightTab;
    }

    public static void setOpenedLeftTab(Class<? extends BaseTab> tabClass)
    {
        openedLeftTab = tabClass;
    }

    public static void setOpenedRightTab(Class<? extends BaseTab> tabClass)
    {
        openedRightTab = tabClass;
    }
}
