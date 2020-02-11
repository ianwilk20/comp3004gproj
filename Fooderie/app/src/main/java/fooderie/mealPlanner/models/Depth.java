package fooderie.mealPlanner.models;

import java.util.ArrayList;
import java.util.List;

public class Depth {
    private int id;
    private String name;
    private boolean editable;

    public static final String WEEK_PLAN = "Week Plans";
    public static final String DAY_PLAN = "Day Plans";
    public static final String MEAL_PLAN = "Meal Plans";
    private static List<Depth> m_depths;

    public static int getId(String n) {
        Depth d = getDepthInfo(n);
        return (d == null) ? -1 : d.getId();
    }

    private static List<Depth> getDepths() {
        if (m_depths == null)
            Depth.init();
        return m_depths;
    }

    private static void init() {
        m_depths = new ArrayList<>();
        m_depths.add(new Depth(0, WEEK_PLAN, true));
        m_depths.add(new Depth(1, DAY_PLAN, false));
        m_depths.add(new Depth(2, MEAL_PLAN, true));
    }


    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isEditable() {
        return editable;
    }

    public Depth(int id, String name, boolean editable) {
        this.id = id;
        this.name = name;
        this.editable = editable;
    }

    public static Depth getDepthInfo(Plan p) {
        int d_id = p.getDepth();
        for (Depth d : Depth.getDepths()) {
            if (d.getId() == d_id) {
                return d;
            }
        }
        return null;
    }

    private static Depth getDepthInfo(String s) {
        for (Depth d : Depth.getDepths()) {
            if (d.getName().equals(s)) {
                return d;
            }
        }
        return null;
    }

    private static Depth getChildDepthInfo(Plan p) {
        Plan child_p = new Plan(p);
        child_p.setDepth( child_p.getDepth() + 1 );
        return getDepthInfo(child_p);
    }

    public static boolean isAtDepth(Plan p, String s) {
        Depth d = Depth.getDepthInfo(p);
        return (d != null && d.getName().equals(s));
    }

    public static boolean isEditableDepth(Plan p) {
        Depth d = Depth.getChildDepthInfo(p);
        if (d != null)
            return d.isEditable();
        return false;
    }

    public static String getAddDialogText(Plan p) {
        Depth d = Depth.getChildDepthInfo(p);
        if (d != null) {
            return "New " + d.getName().substring(0, d.getName().length()-1) + " Name";
        } else {
            return "UNKNOWN";
        }
    }
}
