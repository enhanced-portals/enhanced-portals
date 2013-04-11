package com.alz.enhancedportals.reference;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log
{
    private static Logger logger;
    
    public static void setLogger(Logger log)
    {
        logger = log;
    }
    
    public static void log(Level level, String string)
    {
        logger.log(level, string);
    }
}
