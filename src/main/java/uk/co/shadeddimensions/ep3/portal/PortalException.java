package uk.co.shadeddimensions.ep3.portal;

import uk.co.shadeddimensions.ep3.lib.Localization;

public class PortalException extends Exception
{
    private static final long serialVersionUID = 7990987289131589119L;

    public PortalException(String message, boolean localize)
    {
        super(localize ? Localization.getErrorString(message) : message);
    }
    
    public PortalException(String message)
    {
        super(Localization.getErrorString(message));
    }
}
