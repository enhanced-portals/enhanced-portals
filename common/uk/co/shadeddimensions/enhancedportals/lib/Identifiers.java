package uk.co.shadeddimensions.enhancedportals.lib;

public class Identifiers
{
    public static class Block
    {
        public static int PORTAL_FRAME = 512; // TODO Remove
        public static int PORTAL_BLOCK = 513; // TODO Remove

        private static int STARTING_ID = 256;
        private static int ENDING_ID = 4096;
        private static int COUNTER = 0;

        public static int getNextId()
        {
            COUNTER++;

            return STARTING_ID + COUNTER; // TODO Check if free
        }
    }

    public static class Item
    {
        public static int NETHER_QUARTZ_IGNITER = 5120;
        public static int TEXTURE_DUPLICATOR = 5121;
        public static int WRENCH = 5122;

        private static int STARTING_ID = 4096;
        private static int ENDING_ID = 32000;
        private static int COUNTER = 0;

        public static int getNextId()
        {
            COUNTER++;

            return STARTING_ID + COUNTER; // TODO Check if free
        }
    }

    public static class Gui
    {
        public static int FRAME_CONTROLLER = 1;
    }
}
