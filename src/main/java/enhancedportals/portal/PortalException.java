package enhancedportals.portal;

import enhancedportals.EnhancedPortals;

public class PortalException extends Exception
{
    private static final long serialVersionUID = 7990987289131589119L;

    public PortalException(String message)
    {
        super(EnhancedPortals.localizeError(message));
    }

    public PortalException(String message, boolean localize)
    {
        super(localize ? EnhancedPortals.localizeError(message) : message);
    }
}
