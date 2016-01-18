package jimm.datavision;
import jimm.datavision.source.*;
import jimm.datavision.source.sql.Database;
import jimm.datavision.source.sql.SQLTable;
import jimm.datavision.gui.sql.DbPasswordDialog;
import jimm.datavision.gui.Designer;
import jimm.datavision.gui.StatusDialog;
import jimm.datavision.gui.parameter.ParamAskWin;
import java.awt.Frame;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import org.xml.sax.*;
import org.jruby.Ruby;

/**
 * A report holds data source information, accepts parameters from the user,
 * runs a query, and uses a layout engine to format the output. It may
 * contain many different sections, each of which can contain logic for
 * surpressing itself.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class Report implements Nameable, Writeable {

/**
 * The string to use when reading and writing XML files. Pass it to
 * OutputStreamWriters and InputStreamReaders.
 */
/** Satec */
//public static final String XML_JAVA_ENCODING = "UTF8";
public static final String XML_JAVA_ENCODING = "ISO-8859-1";
/**
 * The string to write at the top of an XML file in the XML decl.
 */
/** Satec */
//public static final String XML_ENCODING_ATTRIBUTE = "UTF-8";
public static final String XML_ENCODING_ATTRIBUTE = "ISO-8859-1";

public static final int AREA_REPORT_HEADER = 0;
public static final int AREA_REPORT_FOOTER = 1;
public static final int AREA_PAGE_HEADER = 2;
public static final int AREA_PAGE_FOOTER = 3;
public static final int AREA_DETAIL = 4;
public static final int AREA_GROUP_HEADER = 5;
public static final int AREA_GROUP_FOOTER = 6;

protected static final double OUTPUT_DTD_VERSION = 1.0;

protected String name;
protected String title;
protected String author;
protected String description;
protected Formula startFormula;
protected DataSource dataSource;
protected HashMap formulas;
protected HashMap parameters;
protected HashMap usercols;
protected HashMap subreports;
protected ArrayList reportHeaders;
protected ArrayList reportFooters;
protected ArrayList pageHeaders;
protected ArrayList pageFooters;
protected ArrayList groups;
protected ArrayList details;
protected Row rset;
protected LayoutEngine layoutEngine;
protected PaperFormat paperFormat;
protected Collection subtotalFields;
protected String databasePassword;
protected boolean askedForParameters;
protected boolean parametersHaveValues;
protected boolean paramsSetManually;
protected boolean dataSourceHasFile;
protected ParameterReader paramReader;
protected boolean caseSensitiveDatabaseNames;	// Flag for Database data sources
protected Ruby ruby;

/** satec */
public Database database;
public Hashtable filterTables= new Hashtable();
private boolean isOracle= false;
/**/

/**
 * Constructs an empty report.
 */
public Report() {
    formulas = new HashMap();
    parameters = new HashMap();
    usercols = new HashMap();
    subreports = new HashMap();
    reportHeaders = new ArrayList();
    reportFooters = new ArrayList();
    pageHeaders = new ArrayList();
    pageFooters = new ArrayList();
    groups = new ArrayList();
    details = new ArrayList();
    name = I18N.get("Report.default_name");
    title = I18N.get("Report.default_title");
    askedForParameters = false;
    parametersHaveValues = false;
    paramsSetManually = false;
    dataSourceHasFile = false;
    caseSensitiveDatabaseNames = true;
    ruby = Ruby.getDefaultInstance();

    initEmptyReport();
}

/**
 * Initialize an empty report. Called from constructors.
 */
protected void initEmptyReport() {
    reportHeaders.add(new Section(this, I18N.get("Report.report_header"),
				  false));
    pageHeaders.add(new Section(this, I18N.get("Report.page_header"),
				false));
    pageFooters.add(new Section(this, I18N.get("Report.page_footer"),
				false));
    reportFooters.add(new Section(this, I18N.get("Report.report_footer"),
				  false));
    details.add(new Section(this, I18N.get("Report.detail"), true));
    paperFormat = PaperFormat.getDefault();
}

/**
 * Sets the layout engine to use.
 *
 * @param layoutEngine a layout engine
 */
public void setLayoutEngine(LayoutEngine layoutEngine) {
    this.layoutEngine = layoutEngine;
    this.layoutEngine.report = this;
}

/**
 * Generates and returns a new unique id number. The number is one larger
 * than the largest in a given list of <code>Identity</code> objects whose
 * identifiers must be <code>Long</code> objects.
 *
 * @param iter an iterator over a collection if <code>Identity</code>
 * objects whose identifiers must be <code>Long</code>s
 * @return a <code>Long</code>
 * @see Identity
 */
protected Long generateNewId(Iterator iter) {
    long max = 0;
    while (iter.hasNext()) {
	Object id = ((Identity)iter.next()).getId();
	long longVal = ((Long)id).longValue();
	if (longVal > max)
	    max = longVal;
    }
    return new Long(max + 1);
}

/**
 * Generates and returns a new unique formula id number.
 *
 * @return a long id
 */
public Long generateNewFormulaId() {
    return generateNewId(formulas.values().iterator());
}

/**
 * Generates and returns a new unique parameter id number.
 *
 * @return a long id
 */
public Long generateNewParameterId() {
    return generateNewId(parameters.values().iterator());
}

/**
 * Generates and returns a new unique user column id number.
 *
 * @return a long id
 */
public Long generateNewUserColumnId() {
    return generateNewId(usercols.values().iterator());
}

/**
 * Generates and returns a new unique user column id number.
 *
 * @return a long id
 */
public Long generateNewSubreportId() {
    return generateNewId(subreports.values().iterator());
}

/**
 * Given an id, returns the field that has that id. If no field with the
 * specified id exists, returns <code>null</code>.
 *
 * @return a field, or <code>null</code> if no field with the specified
 * id exists
 */
public Field findField(final Object id) {
    final Field listOfOne[] = new Field[1];
    listOfOne[0] = null;
    withSectionsDo(new SectionWalker() {
	public void step(Section s) {
	    Field f = s.findField(id);
	    if (f != null) listOfOne[0] = f;
	}
	});
    return listOfOne[0];
}

public String getName() { return name; }
public void setName(String newName) { name = newName; }

public String getTitle() { return title; }
public void setTitle(String newTitle) { title = newTitle; }

public String getAuthor() { return author; }
public void setAuthor(String newAuthor) { author = newAuthor; }

public String getDescription() { return description; }
public void setDescription(String newDescription) {
    description = newDescription;
}

/**
 * Returns the report's start formula; may be <code>null</code>.
 *
 * @return the report's start formula; may be <code>null</code>
 */
public Formula getStartFormula() { return startFormula; }
public void setStartFormula(Formula newStartFormula) {
    startFormula = newStartFormula;
}

public Ruby getRubyInstance() { return ruby; }

public DataSource getDataSource() { return dataSource; }

public boolean hasDataSource() {
    return dataSource != null;
}

/**
 * Sets the data source. Called by <code>ReportReader.database</code>,
 * <code>ReportReader.charSepSource</code>, or user code.
 * <p>
 * If we already have a data source (for example, someone has called
 * <code>setDatabaseConnection</code>), the existing data source
 * will be stomped on. Both <code>ReportReader.database</code> and
 * <code>ReportReader.charSepSource</code> call <code>hasDataSource</code>
 * to check first.
 *
 * @param dataSource a new data source
 * @see ReportReader#database
 * @see ReportReader#charSepSource
 * @see #hasDataSource
 */
public void setDataSource(DataSource newDataSource) {
    dataSource = newDataSource;
}

/**
 * Sets the database connection. If this is called before reading a
 * report XML file, then this connection will be used instead of
 * the connection information specified in the report.
 * <p>
 * <em>Note:</em> this connection will <em>not</em> be closed when
 * the report finishes or even if the database object's connection
 * is reset.
 *
 * @param conn a database connection
 */

public void setDatabaseConnection(Connection conn) throws SQLException {
    dataSource = new Database(conn, this);
    databasePassword = "";	// So user won't be asked for password
}

/** satec */
public void setConexionBD(Connection conn) throws SQLException {
    database= new Database(conn, this);
}


/**
 * Returns the value of the <var>caseSensitiveDatabaseNames</var> flag.
 * By default, this flag is <code>true</code>.
 *
 * @return <code>true</code> if all mixed-case names should be quoted when
 * appropriate
 */
public boolean caseSensitiveDatabaseNames() {
    return caseSensitiveDatabaseNames;
}

/**
 * Sets the value of <var>caseSensitiveDatabaseNames</var>.
 * Normally, any database data sources' query objects quote all mixed-case
 * names where appropriate.
 */
public void setCaseSensitiveDatabaseNames(boolean val) {
    caseSensitiveDatabaseNames = val;
}

/**
 * Tells this report to reload all references to column objects. Called
 * by a database when it resets its connection.
 *
 * @see Database#reset
 */
public void reloadColumns() {
    for (Iterator iter = groups(); iter.hasNext(); ) {
	Group g = (Group)iter.next();
	g.reloadSelectable(dataSource);
    }
    withFieldsDo(new FieldWalker() {
	public void step(Field f) {
	    if (f instanceof ColumnField) {
		ColumnField cf = (ColumnField)f;
		cf.setColumn(dataSource.findColumn(cf.getColumn().getId()));
	    }
	}
	});

    // Let the data source tell its ancillary objects (such as the query)
    // to reload its columns.
    dataSource.reloadColumns();
}

/**
 * Given an id (a column name), returns the column that has that id. If no
 * column with the specified id exists, returns <code>null</code>. Calls
 * <code>DataSource.findColumn</code>.
 *
 * @return acolumn, or <code>null</code> if no column with the specified
 * id exists
 * @see DataSource#findColumn
 */
public Column findColumn(String id) { return dataSource.findColumn(id); }

public PaperFormat getPaperFormat() { return paperFormat; }
public void setPaperFormat(PaperFormat newPaperFormat) {
    paperFormat = newPaperFormat;
}

/**
 * Figure out what <var>obj</var> is and add it.
 */
public void add(Object obj) {
    if (obj instanceof Parameter) addParameter((Parameter)obj);
    else if (obj instanceof Formula) addFormula((Formula)obj);
    else if (obj instanceof UserColumn) addUserColumn((UserColumn)obj);
    else if (obj instanceof Group) addGroup((Group)obj);
    else			// Shouldn't happen
	ErrorHandler.error(I18N.get("Report.add_err_1") + ' '
			   + obj.getClass().getName() + ' '
			   + I18N.get("Report.add_err_2"));
}

/**
 * Figure out what <var>obj</var> is and remove it.
 */
public void remove(Object obj) {
    if (obj instanceof Field)
	removeField((Field)obj);
    else if (obj instanceof Formula)
	removeFormula((Formula)obj);
    else if (obj instanceof Parameter)
	removeParameter((Parameter)obj);
    else if (obj instanceof UserColumn)
	removeUserColumn((UserColumn)obj);
    else if (obj instanceof Group)
	removeGroup((Group)obj);
    else if (obj instanceof Section)
	removeSection((Section)obj);
    else
	ErrorHandler.error(I18N.get("Report.remove_err_1") + ' '
			   + obj.getClass().getName()
			   + I18N.get("Report.remove_err_2"));
}

// ---------------- parameters

/**
 * Returns the parameter with the specified id or <code>null</code>
 * if one is not found. <em>Note</em>: don't use this method if you need
 * a parameter's value. That value is supplied by the user. Call
 * <code>Report.getParameterValue</code> instead, which asks the user to
 * supply the value.
 *
 * @param id the parameter id
 * @return the parameter with that id or <code>null</code> if one is not found
 * @see Report#getParameterValue
 */
public Parameter findParameter(Object id) {
    if (id instanceof String)
	id = new Long((String)id);
    return (Parameter)parameters.get(id);
}

/**
 * Returns the parameter with the specified name or <code>null</code>
 * if one is not found.
 *
 * @param name the name string
 * @return the parameter with that name or <code>null</code> if one is not
 * found
 */
public Parameter findParameterByName(String name) {
    if (name == null || name.length() == 0)
	return null;

    name = name.toLowerCase();
    for (Iterator iter = parameters.values().iterator(); iter.hasNext(); ) {
	Parameter p = (Parameter)iter.next();
	if (name.equals(p.getName().toLowerCase()))
	    return p;
    }
    return null;
}

public void addParameter(Parameter p) {
    parameters.put(p.getId(), p);
}
public void removeParameter(Parameter p) {
    parameters.remove(p.getId());
}
public Iterator parameters() { return parameters.values().iterator(); }

// ---------------- formulas

/**
 * Returns the formula with the specified id or <code>null</code>
 * if one is not found.
 *
 * @param id the formula id
 * @return the formula with that id or <code>null</code> if one is not found
 */
public Formula findFormula(Object id) {
    if (id instanceof String)
	id = new Long((String)id);
    return (Formula)formulas.get(id);
}

/**
 * Returns the formula with the specified name or <code>null</code>
 * if one is not found.
 *
 * @param name the name string
 * @return the formula with that name or <code>null</code> if one is not found
 */
public Formula findFormulaByName(String name) {
    if (name == null || name.length() == 0)
	return null;

    name = name.toLowerCase();
    for (Iterator iter = formulas.values().iterator(); iter.hasNext(); ) {
	Formula f = (Formula)iter.next();
	if (name.equals(f.getName().toLowerCase()))
	    return f;
    }
    return null;
}

public void addFormula(Formula f) {
    formulas.put(f.getId(), f);
}
public void removeFormula(Formula f) {
    formulas.remove(f.getId());
}
public Iterator formulas() { return formulas.values().iterator(); }

// ---------------- user columns

/**
 * Returns the user column with the specified id or <code>null</code>
 * if one is not found.
 *
 * @param id the user column id
 * @return the user column with that id or <code>null</code> if one
 * is not found
 */
public UserColumn findUserColumn(Object id) {
    if (id instanceof String)
	id = new Long((String)id);
    return (UserColumn)usercols.get(id);
}

/**
 * Returns the user column with the specified name or <code>null</code>
 * if one is not found.
 *
 * @param name the name string
 * @return the user column with that name or <code>null</code> if one
 * is not found
 */
public UserColumn findUserColumnByName(String name) {
    if (name == null || name.length() == 0)
	return null;

    name = name.toLowerCase();
    for (Iterator iter = usercols.values().iterator(); iter.hasNext(); ) {
	UserColumn f = (UserColumn)iter.next();
	if (name.equals(f.getName().toLowerCase()))
	    return f;
    }
    return null;
}

public void addUserColumn(UserColumn uc) {
    usercols.put(uc.getId(), uc);
}
public void removeUserColumn(UserColumn uc) {
    usercols.remove(uc.getId());
}
public Iterator userColumns() { return usercols.values().iterator(); }

// ---------------- subreports

/**
 * Returns the subreport with the specified id or <code>null</code>
 * if one is not found.
 *
 * @param id the subreport id
 * @return the subreport with that id or <code>null</code> if one is not found
 */
public Subreport findSubreport(Object id) {
    if (id instanceof String)
	id = new Long((String)id);
    return (Subreport)subreports.get(id);
}

public void addSubreport(Subreport sub) {
    subreports.put(sub.getId(), sub);
}
public void removeSubreport(Subreport sub) {
    subreports.remove(sub.getId());
}
public Iterator subreports() { return subreports.values().iterator(); }

// ---------------- selectable methods

public Selectable findSelectable(Object id, String type) {
    if ("column".equals(type))
	return findColumn(id.toString());
    else			// "usercol"
	return findUserColumn(id);
}

// ---------------- section manipulation

/**
 * Returns <code>true</code> if this report contains the specified section.
 *
 * @return <code>true</code> if this report contains the specified section
 */
public boolean contains(Section s) {
    switch (s.getArea()) {
    case AREA_REPORT_HEADER: return reportHeaders.contains(s);
    case AREA_REPORT_FOOTER: return reportFooters.contains(s);
    case AREA_PAGE_HEADER: return pageHeaders.contains(s);
    case AREA_PAGE_FOOTER: return pageFooters.contains(s);
    case AREA_DETAIL: return details.contains(s);
    case AREA_GROUP_HEADER:
	for (Iterator iter = groups(); iter.hasNext(); )
	    if (((Group)iter.next()).headerSections.contains(s)) return true;
	return false;
    case AREA_GROUP_FOOTER:
	for (Iterator iter = groups(); iter.hasNext(); )
	    if (((Group)iter.next()).footerSections.contains(s)) return true;
	return false;
    default:			// Should never happen
	return false;
    }
}

/**
 * Returns the first section in the list of the specified type. If the
 * type is <code>GROUP_HEADER</code> or <code>GROUP_FOOTER</code>, return
 * (a) <code>null</code> if there are no groups in the report or
 * (b) the first header or footer of the first group.
 * <p>
 * Used by <code>FieldClipping</code> when trying to paste a field into
 * either some other report or the same report if the original section
 * is no longer in that report.
 *
 * @return a section; may be <code>null</code>
 * @see jimm.datavision.gui.cmd.FieldClipping
 */
public Section getFirstSectionByArea(int area) {
    switch (area) {
    case AREA_REPORT_HEADER: return (Section)reportHeaders.get(0);
    case AREA_REPORT_FOOTER: return (Section)reportFooters.get(0);
    case AREA_PAGE_HEADER: return (Section)pageHeaders.get(0);
    case AREA_PAGE_FOOTER: return (Section)pageFooters.get(0);
    case AREA_DETAIL: return (Section)details.get(0);
    case AREA_GROUP_HEADER:
	if (groups.size() == 0) return null;
	return (Section)((Group)groups.get(0)).headers().next();
    case AREA_GROUP_FOOTER:
	if (groups.size() == 0) return null;
	return (Section)((Group)groups.get(0)).footers().next();
    }
    return null;
}

/**
 * Adds a (possibly created) section after <var>afterThis</var> and
 * returns the section. If <var>section</var> is <code>null</code>,
 * a new section will be created.
 *
 * @param section the section to insert; if <code>null</code>, a new
 * section will be created
 * @param afterThis the new section will be inserted after this one
 * @param list the list in which <var>afterThis</var> is found
 * @param nameKey the new section name's I18N lookup key
 * @see I18N
 */
protected Section insertSectionAfter(Section section, Section afterThis,
				     ArrayList list, String nameKey)
{
    int i = list.indexOf(afterThis);
    if (section == null)
	section = new Section(this, I18N.get(nameKey), false);
    list.add(i + 1, section);
    return section;
}

/**
 * Returns a structure useful only by this report for re-inserting
 * a section. This structure may later be passed to
 * <code>reinsertHeader</code>. Used by <code>DeleteSectionCommand</code>.
 *
 * @param section the section in question
 * @return section location information
 * @see jimm.datavision.gui.cmd.DeleteSectionCommand
 */
public ReportSectionLoc getSectionLocation(Section s) {
    List list = null;
    int index = -1;

    switch (s.getArea()) {
    case AREA_REPORT_HEADER: list = reportHeaders; break;
    case AREA_REPORT_FOOTER: list = reportFooters; break;
    case AREA_PAGE_HEADER: list = pageHeaders; break;
    case AREA_PAGE_FOOTER: list = pageFooters; break;
    case AREA_DETAIL: list = details; break;
    case AREA_GROUP_HEADER:
	for (Iterator iter = groups(); iter.hasNext(); ) {
	    Group g = (Group)iter.next();
	    if (g.headerSections.contains(s))
		list = g.headerSections;
	}
	break;
    case AREA_GROUP_FOOTER:
	for (Iterator iter = groups(); iter.hasNext(); ) {
	    Group g = (Group)iter.next();
	    if (g.footerSections.contains(s))
		list = g.footerSections;
	}
	break;
    }

    return new ReportSectionLoc(s, list, list.indexOf(s));
}

/**
 * Reinserts a section based on the location information previously
 * retrieved by a call to <code>getSectionLocation</code>. Used by
 * <code>DeleteSectionCommand</code>.
 *
 * @param loc a section locatction
 * @see jimm.datavision.gui.cmd.DeleteSectionCommand
 */
public void reinsertSection(ReportSectionLoc loc) {
    loc.list.add(loc.index, loc.section);
}

// ---------------- headers

public void addHeader(Section s) {
    reportHeaders.add(s);
    s.setArea(AREA_REPORT_HEADER);
}
public Section insertHeaderAfter(Section s, Section afterThis) {
    s.setArea(AREA_REPORT_HEADER);
    return insertSectionAfter(s, afterThis, reportHeaders,
			      "Report.report_header");
}
public void removeHeader(Section s) {
    reportHeaders.remove(s);
}
public Iterator headers() { return reportHeaders.iterator(); }
/** Used for testing only. */
public int testCountHeaders() { return reportHeaders.size(); }

// ---------------- footers

public void addFooter(Section s) {
    reportFooters.add(s);
    s.setArea(AREA_REPORT_FOOTER);
}
public Section insertFooterAfter(Section s, Section afterThis) {
    s.setArea(AREA_REPORT_FOOTER);
    return insertSectionAfter(s, afterThis, reportFooters,
			      "Report.report_footer");
}
public void removeFooter(Section s) {
    reportFooters.remove(s);
}
public Iterator footers() { return reportFooters.iterator(); }
/** Used for testing only. */
public int testCountFooters() { return reportFooters.size(); }

// ---------------- page headers

public void addPageHeader(Section s) {
    pageHeaders.add(s);
    s.setArea(AREA_PAGE_HEADER);
}
public Section insertPageHeaderAfter(Section s, Section afterThis) {
    s.setArea(AREA_PAGE_HEADER);
    return insertSectionAfter(s, afterThis, pageHeaders, "Report.page_header");
}
public void removePageHeader(Section s) {
    pageHeaders.remove(s);
}
public Iterator pageHeaders() { return pageHeaders.iterator(); }
/** Used for testing only. */
public int testCountPageHeaders() { return pageHeaders.size(); }

// ---------------- page footers

public void addPageFooter(Section s) {
    pageFooters.add(s);
    s.setArea(AREA_PAGE_FOOTER);
}
public Section insertPageFooterAfter(Section s, Section afterThis) {
    s.setArea(AREA_PAGE_FOOTER);
    return insertSectionAfter(s, afterThis, pageFooters, "Report.page_footer");
}
public void removePageFooter(Section s) {
    pageFooters.remove(s);
}
public Iterator pageFooters() { return pageFooters.iterator(); }
/** Used for testing only. */
public int testCountPageFooters() { return pageFooters.size(); }

// ---------------- groups

public void addGroup(Group g) {
    groups.add(g);
    dataSource.removeSort(g.getSelectable());
}
public void removeGroup(Group g) { groups.remove(g); }
public void removeAllGroups() { groups.clear(); }
public Iterator groups() { return groups.iterator(); }
public int countGroups() { return groups.size(); }
public boolean hasGroups() { return countGroups() > 0; }

/**
 * Returns the last (innermost) group in the report, or <code>null</code>
 * if there are no groups.
 *
 * @return the last (innermost) group in the report, or <code>null</code>
 * if there are no groups.
 */
public Group innermostGroup() {
    return groups.size() > 0 ? (Group)groups.get(groups.size() - 1) : null;
}

/**
 * Returns an iterator over the groups in reverse order. Useful when
 * displaying group footers.
 *
 * @return an iterator over the groups, in reverse order
 */
public Iterator groupsReversed() {
    ArrayList reversed = (ArrayList)groups.clone();
    Collections.reverse(reversed);
    return reversed.iterator();
}

// ---------------- details

public void addDetail(Section s) {
    details.add(s);
    s.setArea(AREA_DETAIL);
}
public Section insertDetailAfter(Section s, Section afterThis) {
    s.setArea(AREA_DETAIL);
    return insertSectionAfter(s, afterThis, details, "Report.detail");
}
public void removeDetail(Section s) { details.remove(s); }
public Iterator details() { return details.iterator(); }
/** Used for testing only. */
public int testCountDetails() { return details.size(); }

public void removeField(Field f) {
    Section s = sectionContaining(f);
    if (s != null)
	s.removeField(f);
}

/**
 * Returns <code>true</code> if the specified field exists within this
 * report.
 *
 * @param f a field
 * @return <code>true</code> if the specified field exists within this
 * report
 */
public boolean contains(Field f) {
    return sectionContaining(f) != null;
}

/**
 * Returns the section containing the specified field, or <code>null</code>
 * if the field is not in the report.
 *
 * @param f a field
 * @return the section containing the specified field, or <code>null</code>
 * if the field is not in the report
 */
public Section sectionContaining(Field f) {
    Section s;
    Iterator iter, iter2;
    for (iter = reportHeaders.iterator(); iter.hasNext(); ) {
	s = (Section)iter.next();
	if (s.contains(f)) return s;
    }
    for (iter = pageHeaders.iterator(); iter.hasNext(); ) {
	s = (Section)iter.next();
	if (s.contains(f)) return s;
    }
    for (iter = groups.iterator(); iter.hasNext(); ) {
	for (iter2 = ((Group)iter.next()).headerSections.iterator();
	     iter2.hasNext(); ) {
	    s = (Section)iter2.next();
	    if (s.contains(f)) return s;
	}
    }
    for (iter = details.iterator(); iter.hasNext(); ) {
	s = (Section)iter.next();
	if (s.contains(f)) return s;
    }
    for (iter = groups.iterator(); iter.hasNext(); ) {
	for (iter2 = ((Group)iter.next()).footerSections.iterator();
	     iter2.hasNext(); ) {
	    s = (Section)iter2.next();
	    if (s.contains(f)) return s;
	}
    }
    for (iter = reportFooters.iterator(); iter.hasNext(); ) {
	s = (Section)iter.next();
	if (s.contains(f)) return s;
    }
    for (iter = pageFooters.iterator(); iter.hasNext(); ) {
	s = (Section)iter.next();
	if (s.contains(f)) return s;
    }

    return null;
}

/**
 * Returns <code>true</code> if the specified field exists within this
 * report either directly (as a field) or indirectly (as a formula used
 * by a subtotal, parameter, user column, or another formula).
 *
 * @param f a field
 * @return <code>true</code> if the specified field exists within this
 * report
 */
public boolean containsReferenceTo(final Field f) {
    if (startFormula != null && startFormula.refersTo(f))
	return true;

    final boolean[] answer = new boolean[1];
    answer[0] = false;

    withSectionsDo(new SectionWalker() {
	public void step(Section s) {
	    if (s.containsReferenceTo(f))
		answer[0] = true;
	}
	});

    return answer[0];
}

/**
 * Returns <code>true</code> if the specified formula exists within this
 * report either directly (as a formula field) or indirectly (as a formula
 * used by a subtotal or by another formula). This is not the same as
 * answering the question, "Does this report contain this formula?" because
 * we want to know if the visual portion of the report or some other formula
 * accesses the specified formula.
 *
 * @param f a formula
 * @return <code>true</code> if the specified parameter exists within some
 * visual element of the report or within some formula
 */
public boolean containsReferenceTo(final Formula f) {
    if (startFormula != null && startFormula.refersTo(f))
	return true;

    final boolean[] answer = new boolean[1];
    answer[0] = false;

    withSectionsDo(new SectionWalker() {
	public void step(Section s) {
	    if (s.containsReferenceTo(f))
		answer[0] = true;
	}
	});

    return answer[0];
}

/**
 * Returns <code>true</code> if the specified user column exists within
 * this report either directly (as a user column field) or indirectly
 * (inside a subtotal or formula). This is not the same as answering the
 * question, "Does this report contain this user column?" because we want
 * to know if the visual portion of the report or some formula accesses the
 * specified user column.
 *
 * @param uc a user column
 * @return <code>true</code> if the specified parameter exists within some
 * visual element of the report or within some formula
 */
public boolean containsReferenceTo(final UserColumn uc) {
    if (startFormula != null && startFormula.refersTo(uc))
	return true;

    final boolean[] answer = new boolean[1];
    answer[0] = false;

    withSectionsDo(new SectionWalker() {
	public void step(Section s) {
	    if (s.containsReferenceTo(uc))
		answer[0] = true;
	}
	});

    return answer[0];
}

/**
 * Returns <code>true</code> if the specified parameter exists within this
 * report either directly (as a parameter field) or indirectly (as a
 * parameter used by a subtotal or by another parameter or in the query's
 * where clause). This is not the same as answering the question, "Does
 * this report contain this parameter?" because we want to know if the
 * visual portion of the report or some formula accesses the specified
 * parameter.
 *
 * @param p a parameter
 * @return <code>true</code> if the specified parameter exists within some
 * visual element of the report or within some formula or within the
 * query's where clause
 */
public boolean containsReferenceTo(final Parameter p) {
    if (startFormula != null && startFormula.refersTo(p))
	return true;

    if (dataSource.containsReferenceTo(p))
	return true;

    final boolean[] answer = new boolean[1];
    answer[0] = false;

    withSectionsDo(new SectionWalker() {
	public void step(Section s) {
	    if (s.containsReferenceTo(p))
		answer[0] = true;
	}
	});

    return answer[0];
}

/**
 * Returns a set of all the parameters actually used in the
 * report. Calls <code>containsReferenceTo</code>.
 *
 * @return a list of parameters
 * @see #containsReferenceTo
 */
protected List collectUsedParameters() {
    ArrayList list = new ArrayList();
    for (Iterator iter = parameters.values().iterator(); iter.hasNext(); ) {
	Parameter p = (Parameter)iter.next();
	if (containsReferenceTo(p))
	    list.add(p);
    }
    return list;
}

/**
 * Returns the group associated with the specified column, or
 * <code>null</code> if there isn't one.
 *
 * @param selectable a selectable field
 * @return the group associated with this column, or <code>null</code>
 * if there isn't one
 */
public Group findGroup(Selectable selectable) {
    for (Iterator iter = groups(); iter.hasNext(); ) {
	Group g = (Group)iter.next();
	if (g.getSelectable() == selectable)
	    return g;
    }
    return null;
}

/**
 * Returns the group associated with the specified section, or
 * <code>null</code> if there isn't one.
 *
 * @param section a section
 * @return the group associated with this section, or <code>null</code>
 * if there isn't one
 */
public Group findGroup(Section section) {
    for (Iterator iter = groups(); iter.hasNext(); ) {
	Group group = (Group)iter.next();
	if (group.contains(section))
	    return group;
    }
    return null;
}

/**
 * Returns <code>true</code> if the specified section is contained in
 * any group. The section may be either a header or a footer section.
 *
 * @param section a section
 * @return <code>true</code> if the section is within some group
 */
public boolean isInsideGroup(Section section) {
    return findGroup(section) != null;
}

/**
 * Returns <code>true</code> if the specified data source column is a
 * group column.
 *
 * @return <code>true</code> if the specified data source column is a
 * group column
 */
public boolean isUsedBySomeGroup(Selectable g) {
    return findGroup(g) != null;
}

/**
 * Creates and returns a new section below the specified one.
 *
 * @param goBelowThis a section
 * @return a new section
 */
public Section insertSectionBelow(Section goBelowThis) {
    return insertSectionBelow(null, goBelowThis);
}

/**
 * Inserts a (possibly newly created) section below the specified one.
 * Returns the inserted section.
 *
 * @param section the section to insert
 * @param goBelowThis <var>section</var> goes below this one; 
 * @return a new section
 */
public Section insertSectionBelow(Section section, Section goBelowThis) {
    if (section == null)
	section = new Section(this, goBelowThis.getArea(),
			      goBelowThis.getName(),
			      goBelowThis.isDetail);

    if (goBelowThis == null)
	return insertHeaderAfter(section, null);

    if (reportHeaders.contains(goBelowThis))
	return insertHeaderAfter(section, goBelowThis);
    else if (reportFooters.contains(goBelowThis))
	return insertFooterAfter(section, goBelowThis);
    else if (pageHeaders.contains(goBelowThis))
	return insertPageHeaderAfter(section, goBelowThis);
    else if (pageFooters.contains(goBelowThis))
	return insertPageFooterAfter(section, goBelowThis);
    else if (details.contains(goBelowThis))
	return insertDetailAfter(section, goBelowThis);

    for (Iterator iter = groups(); iter.hasNext(); ) {
	Group g = (Group)iter.next();
	if (g.headerSections.contains(goBelowThis))
	    return g.insertHeaderAfter(section, goBelowThis);
	else if (g.footerSections.contains(goBelowThis))
	    return g.insertFooterAfter(section, goBelowThis);
    }

    return null;		// Should not happen
}

/**
 * Removes the specified section.
 *
 * @param s a section
 */
public void removeSection(Section s) {
    if (reportHeaders.contains(s))
	removeHeader(s);
    else if (reportFooters.contains(s))
	removeFooter(s);
    else if (pageHeaders.contains(s))
	removePageHeader(s);
    else if (pageFooters.contains(s))
	removePageFooter(s);
    else if (details.contains(s))
	removeDetail(s);
    else {
	for (Iterator iter = groups(); iter.hasNext(); ) {
	    Group g = (Group)iter.next();
	    if (g.headerSections.contains(s)) {
		g.removeHeader(s);
		return;
	    }
	    if (g.footerSections.contains(s)) {
		g.removeFooter(s);
		return;
	    }
	}
    }
}

/**
 * Returns <code>true</code> if this report contains some field anywhere.
 * Useful when the GUI is enabling menu items, for example.
 *
 * @return <code>true</code> if this report contains some field anywhere
 */
public boolean hasFields() {
    final boolean[] answer = new boolean[1];
    answer[0] = false;
    withFieldsDo(new FieldWalker() {
	public void step(Field f) {
	    answer[0] = true;
	}
	});
    return answer[0];
}

/**
 * Returns <code>true</code> if this report contains some parameter field
 * anywhere. This doesn't determine if any parameters have been created,
 * but rather if any of the parameters are actually used in the report.
 * <p>
 * This method is not used by DataVision, but is useful for apps embedding
 * DataVision that want to answer the question, "Do I have to read in
 * a parameter XML file?"
 *
 * @return <code>true</code> if this report contains some parameter field
 * anywhere
 */
public boolean hasParameterFields() {
    final boolean answer[] = new boolean[1];
    answer[0] = false;
    withFieldsDo(new FieldWalker() {
	public void step(Field f) {
	    if (f instanceof ParameterField) answer[0] = true;
	}
	});
    return answer[0];
}

/**
 * Returns <code>true</code> if the specified section is the only section
 * of its kind; that is, the only section in the collection in which it
 * is contained.
 *
 * @param s a report section
 * @return <code>true</code> if this section is a loner
 */
public boolean isOneOfAKind(Section s) {
    if (reportHeaders.contains(s))
	return reportHeaders.size() == 1;
    if (reportFooters.contains(s))
	return reportFooters.size() == 1;
    if (pageHeaders.contains(s))
	return pageHeaders.size() == 1;
    if (pageFooters.contains(s))
	return pageFooters.size() == 1;
    if (details.contains(s))
	return details.size() == 1;

    for (Iterator iter = groups(); iter.hasNext(); ) {
	Group g = (Group)iter.next();
	if (g.headerSections.contains(s))
	    return g.headerSections.size() == 1;
	if (g.footerSections.contains(s))
	    return g.footerSections.size() == 1;
    }

    return false;
}

/**
 * Sets the database password.
 */
public void setDatabasePassword(String pwd) { databasePassword = pwd; }

/**
 * Sets a database's user name and password. Called from
 * <code>Database.initializeConnection</code>. If we already have the
 * password, give it to the database. If we don't, ask the user for
 * both the user name (supplied to us) and the password and give them
 * to the database.
 *
 * @param db the database
 * @see Database#initializeConnection
 */
public void askForPassword(Database db) {
    if (databasePassword != null) {
	db.setPassword(databasePassword);
	return;
    }

    if (ErrorHandler.usingGUI()) {
	DbPasswordDialog dialog =
	    new DbPasswordDialog(getDesignFrame(), db.getName(),
				 db.getUserName());
	// This dialog is modal, so when we return the values have been
	// filled.
	db.setUserName(dialog.getUserName());
	db.setPassword(dialog.getPassword());
    }
    else {
	System.out.print(I18N.get("Report.user_name") + " ["
			 + db.getUserName() + "]: ");
	System.out.flush();
	try {
	    BufferedReader in =
		new BufferedReader(new InputStreamReader(System.in));
	    String username = in.readLine();
	    if (username.length() > 0)
		db.setUserName(username);
	}
	catch (IOException e) {
	    ErrorHandler.error(I18N.get("Report.user_name_err"));
	}

	System.out.print(I18N.get("Report.password") + ' ' + db.getUserName()
			 + ": ");
	System.out.flush();
	try {
	    BufferedReader in =
		new BufferedReader(new InputStreamReader(System.in));
	    databasePassword = in.readLine();
	    db.setPassword(databasePassword);
	}
	catch (IOException e) {
	    ErrorHandler.error(I18N.get("Report.password_err"));
	}
    }
}

/**
 * Returns the value of the specified parameter. First time at the start
 * of each report run, asks user for parameter values.
 *
 * @param paramId a parameter id
 */
public Object getParameterValue(Object paramId) {
    if (!askedForParameters)
	askForParameters();

    return findParameter(paramId).getValue();
}

/**
 * Lets the caller tell the report to ask for parameters (<var>val</var> is
 * <code>false</code>, the default value) or to not ask (<var>val</var> is
 * <code>true</code>). Call this method with <var>val</var> <code>=
 * true</code> when you set the parameters in your Java code manually.
 */ 
public void parametersSetManually(boolean val) { paramsSetManually = val; }

/**
 * Asks the user for parameter values. If the user cancels, we throw a
 * <code>UserCancellationException</code>.
 */
protected void askForParameters() throws UserCancellationException {
    List usedParameters = null;
    if (askedForParameters || paramsSetManually
	|| (usedParameters = collectUsedParameters()).size() == 0)
    {
	askedForParameters = true;
	return;
    }

    if (ErrorHandler.usingGUI()) {
	if (parametersHaveValues) {
	    // Ask user about re-using the previous values
	    String msg = I18N.get("Report.use_prev_param_vals");
	    if (JOptionPane.showConfirmDialog(getDesignFrame(), msg,
					      I18N.get("Report.use_prev_title"),
					      JOptionPane.YES_NO_OPTION)
		== JOptionPane.YES_OPTION)
		{
		    askedForParameters = true;
		    parametersHaveValues = true;
		    return;
		}
	}

	// This dialog is modal. By the time it returns, the parameters
	// have their new values.
	    
	if (new ParamAskWin(getDesignFrame(), usedParameters).userCancelled())
	    throw new UserCancellationException(I18N.get("Report.user_cancelled"));
    }
    else {
	if (paramReader == null) {
	    ErrorHandler.error(I18N.get("Report.missing_param_xml_file"));
	    throw new UserCancellationException(I18N.get("Report.missing_param_xml_file_short"));
	}
	try {
	    paramReader.read();
	    paramReader = null;
	}
	catch (Exception ex) {
	    ErrorHandler.error(I18N.get("Report.param_file_err_1") + ' '
			       + paramReader.getInputName()
			       + I18N.get("Report.param_file_err_2"), ex);
	    throw new UserCancellationException(I18N.get("Report.param_file_err_short"));
	}
    }

    askedForParameters = true;
    parametersHaveValues = true;
}

/**
 * Asks the user for a data source file if necessary. If the user cancels,
 * we throw a <code>UserCancellationException</code>.
 */
protected void askForDataSourceFile()
    throws UserCancellationException, FileNotFoundException
{
    if (!dataSource.needsSourceFile() || !ErrorHandler.usingGUI())
	return;

    if (dataSourceHasFile) {
	// Ask user about re-using the previous values
	String msg = I18N.get("Report.use_prev_data_source_file");
	if (JOptionPane.showConfirmDialog(getDesignFrame(), msg,
					  I18N.get("Report.use_prev_data_source_title"),
					  JOptionPane.YES_NO_OPTION)
	    == JOptionPane.YES_OPTION)
	    {
		dataSource.reuseSourceFile();
		return;
	    }
    }

    Frame frame = Designer.findWindowFor(this) == null ? null
	: Designer.findWindowFor(this).getFrame();
    JFileChooser chooser = Designer.getChooser();
    int returnVal = chooser.showOpenDialog(frame);
    if (returnVal == JFileChooser.APPROVE_OPTION)
	dataSource.setSourceFile(chooser.getSelectedFile().getPath());
    else
	throw new UserCancellationException(I18N.get("Report.user_cancelled"));

    dataSourceHasFile = true;
}

/**
 * Spawns a new thread that runs the report, using the layout engine
 * to generate output. The new thread runs the method
 * <code>runReport</code>.
 * <p>
 * You may ask why this method is called <code>run</code> and it runs
 * another method called <code>runReport</code> when the normal convention
 * is for a thread to run a <code>Runnable</code> object that has a
 * <code>run</code> method. The reason is purely historical. This method
 * started non-threaded, and I don't want anyone else who relies on this
 * method to have to change their code.
 *
 * @see #runReport
 */
public void run() {
    new Thread(new Runnable() {
	public void run() { runReport(); }
	}).start();
}

/**
 * Runs the data source's query and hands rows to the layout engine. This
 * method is called from <code>run</code>, which spawns a new thread
 * in which this method is run. If you want to run a report, decide
 * if you want it to run in a separate thread or not. If so, call
 * <code>run</code>. If not, use this method.
 *
 * @see #run
 */
public void runReport() {
    // Parameters
    askedForParameters = false;	// Ask user again
    try {
	askForDataSourceFile();
	askForParameters();
    }
    catch (UserCancellationException e) { // Ignore
	return;
    }
    catch (IOException ioe) {	// Handled when parsed as XML
	return;
    }

    // Pre-report initialization
    for (Iterator iter = groups.iterator(); iter.hasNext(); )
	((Group)iter.next()).reset();
    collectSubtotalFields();
    if (startFormula != null)
	startFormula.eval();
    for (Iterator iter = formulas(); iter.hasNext(); )
	((Formula)iter.next()).useCache();
    resetFormulas();

    // Run the report
    Connection conn = null;
    rset = null;
    StatusDialog statusDialog = null;

    try {
	if (ErrorHandler.usingGUI()) {
	    statusDialog = new StatusDialog(getDesignFrame(),
					    I18N.get("Report.status_title"),
					    true,
					    I18N.get("Report.status_running"));
	}

	if (!layoutEngine.wantsMoreData())
	    return;

	rset = dataSource.execute();

	boolean layoutStarted = false;
	while (layoutEngine.wantsMoreData() && rset.next()) {
	    if (statusDialog != null) {
		if (statusDialog.isCancelled())
		    throw new UserCancellationException();
		statusDialog.update(I18N.get("Report.processing_row") + ' '
				    + rowNumber());
	    }

	    if (!layoutStarted) {
		layoutEngine.start();
		layoutStarted = true;
	    }
	    processResultRow();
	}

	rset.last();		// Recall last row so we can access the data
	if (!layoutStarted) {	// No rows in report
	    layoutEngine.start();
	    layoutEngine.end();
	}
	else {			// Output group footers and end of report
	    layoutEngine.groupFooters(true);
	    layoutEngine.end();
	}
    }
    catch (UserCancellationException uce) {
	layoutEngine.cancel();
    }
    catch (SQLException sqle) {
	layoutEngine.cancel();
	ErrorHandler.error(dataSource.getQuery().toString(), sqle);
    }
    catch (Exception e) {
	layoutEngine.cancel();
	ErrorHandler.error(e);
    }
    finally {
	if (rset != null) rset.close();

	subtotalFields = null;
	if (statusDialog != null)
	    statusDialog.dispose();
    }
}

/**
 * Returns the <code>Frame</code> associated with the design window for
 * this report; may be <code>null</code>.
 *
 * @return a <code>Frame</code>; may be <code>null</code>
 */
protected Frame getDesignFrame() {
    Designer d = Designer.findWindowFor(this);
    return d == null ? null : d.getFrame();
}

/**
 * Processes a single data source row. Note that the <code>next</code>
 * method of the result set has already been called.
 */
protected void processResultRow() throws java.sql.SQLException {
    resetFormulas();
    updateGroups();

    // To output footers, bring back the previous row of data
    if (!rset.isFirst()) {
	rset.previous();
	layoutEngine.groupFooters(false);
	rset.next();
	resetFormulas();
    }

    updateGroupCounters();
    updateSubtotals();

    boolean isLastRow = rset.isLast();
    layoutEngine.groupHeaders(isLastRow);
    layoutEngine.detail(isLastRow);
}

/**
 * Tells each formula that it should re-evaluate.
 */
protected void resetFormulas() {
    for (Iterator iter = formulas(); iter.hasNext(); )
	((Formula)iter.next()).shouldEvaluate();
}

/**
 * Evalues the formulas in the specified section. This is called by the
 * layout engine just before the section gets output.
 *
 * @param s a section
 */
void evaluateFormulasIn(Section s) {
    for (Iterator iter = s.fields(); iter.hasNext(); ) {
	Field f = (Field)iter.next();
	if (f instanceof FormulaField)
	    ((FormulaField)f).getValue(); // Force evaluation
    }
}

/**
 * Returns the current value of the specified selectable. Only defined
 * when running a report.
 *
 * @return the string or Double value of the column
 */
/* Satec
public Object columnValue(Selectable selectable) {
    // Ask data source for field number, then get value of that column
    return rset.getObject(dataSource.indexOfSelectable(selectable) + 1);
}
*/

/** Satec */
/*   Permite visualizar correctamente los tipos de dato oracle.sql.TIMESTAMP
     al ejecutar la plantilla desde las aplicaciones cliente.
*/

public Object columnValue(Selectable selectable) {
    // Ask data source for field number, then get value of that column
    try{
        if (isOracle){
            if ((rset.getObject(dataSource.indexOfSelectable(selectable) + 1) != null) && (rset.getObject(dataSource.indexOfSelectable(selectable) + 1) instanceof oracle.sql.TIMESTAMP)){
                return oracle.sql.TIMESTAMP.toDate(((oracle.sql.TIMESTAMP)rset.getObject(dataSource.indexOfSelectable(selectable) + 1)).getBytes());
            }else{
                return rset.getObject(dataSource.indexOfSelectable(selectable) + 1);
            }
        }else{
            return rset.getObject(dataSource.indexOfSelectable(selectable) + 1);
        }
    }catch(SQLException e){
        return rset.getObject(dataSource.indexOfSelectable(selectable) + 1);
    }
}
/**/

/**
 * Returns the current page number. Asks the layout engine. Only defined
 * when running a report.
 *
 * @return a page number
 */
public int pageNumber() {
    return layoutEngine.pageNumber;
}

/**
 * Returns the current data row number. Only defined when running a report.
 */
public int rowNumber() {
    return rset.getRow();
}

/**
 * Returns the current row of data. Only defined when running a report.
 */
public Row getCurrentRow() {
    return rset;
}

/**
 * Iterates over all sections in the report, passing the section to the
 * specified <code>SectionWalker</code>. The sections are visited in the
 * order in which they will be displayed.
 *
 * @param s a section walker
 */
public void withSectionsDo(SectionWalker s) {
    for (Iterator iter = reportHeaders.iterator(); iter.hasNext(); )
	s.step((Section)iter.next());
    for (Iterator iter = pageHeaders.iterator(); iter.hasNext(); )
	s.step((Section)iter.next());
    for (Iterator iter = groups.iterator(); iter.hasNext(); )
	for (Iterator iter2 = ((Group)iter.next()).headerSections.iterator();
	     iter2.hasNext(); )
	    s.step((Section)iter2.next());
    for (Iterator iter = details.iterator(); iter.hasNext(); )
	s.step((Section)iter.next());
    for (Iterator iter = groups.iterator(); iter.hasNext(); )
	for (Iterator iter2 = ((Group)iter.next()).footerSections.iterator();
	     iter2.hasNext(); )
	    s.step((Section)iter2.next());
    for (Iterator iter = reportFooters.iterator(); iter.hasNext(); )
	s.step((Section)iter.next());
    for (Iterator iter = pageFooters.iterator(); iter.hasNext(); )
	s.step((Section)iter.next());
}

/**
 * Iterates over all fields in the report, passing the section to the
 * specified <code>FieldWalker</code>.
 *
 * @param f a field walker
 */
public void withFieldsDo(final FieldWalker f) {
    withSectionsDo(new SectionWalker() {
	public void step(Section s) {
	    for (Iterator it = s.fields(); it.hasNext(); )
		f.step((Field)it.next());
	}
	});
}

/**
 * Collects all subtotal fields and lets each one initialize itself.
 * Used once at the beginning of each run.
 */
protected void collectSubtotalFields() {
    subtotalFields = new ArrayList();
    withFieldsDo(new FieldWalker() {
	public void step(Field f) {
	    if (f instanceof SubtotalField) {
		((SubtotalField)f).initialize();
		subtotalFields.add(f);
	    }
	}
	});
}

/**
 * Collects all subtotal fields that are subtotaling the specified field.
 * Used by the report design GUI.
 * 
 * @param field a field
 * @return a list of subtotal fields
 */
public AbstractList getSubtotalFieldsFor(final Field field) {
    final ArrayList subs = new ArrayList();
    withFieldsDo(new FieldWalker() {
	public void step(Field f) {
	    if (f instanceof SubtotalField
		&& ((SubtotalField)f).getField() == field)
	    {
		subs.add(f);
	    }
	}
	});
    return subs;
}

/**
 * Updates each subtotal field.
 */
protected void updateSubtotals() {
    for (Iterator iter = subtotalFields.iterator(); iter.hasNext(); )
	((SubtotalField)iter.next()).updateSubtotal();
}

/**
 * Updates each group's value based on the current value of the column
 * each group uses.
 */
protected void updateGroups() {
    for (Iterator iter = groups.iterator(); iter.hasNext(); ) {
	Group g = (Group)iter.next();
	g.setValue(this);
    }
}

/**
 * Lets each group update its line counter.
 */
protected void updateGroupCounters() {
    for (Iterator iter = groups.iterator(); iter.hasNext(); ) {
	Group g = (Group)iter.next();
	g.updateCounter();
    }
}

/**
 * This is the old name of <code>setParameterXMLInput(String)</code>.
 * Use that instead. Remembers name of parameter XML file.
 *
 * @param fileName an XML file name
 * @see #setParameterXMLInput(String)
 */
public void setParameterXMLFile(String fileName)
{
    setParameterXMLInput(fileName);
}

/**
 * Remembers name of parameter XML file.
 *
 * @param fileName an XML file name
 */
public void setParameterXMLInput(String fileName)
{
    paramReader = new ParameterReader(this, fileName);
}

/**
 * Remebers reader for parameter XML.
 *
 * @param in the reader
 */
public void setParameterXMLInput(Reader in) throws IOException, SAXException {
    paramReader = new ParameterReader(this, in);
}

/**
 * Remembers input stream reader for parameter XML.
 *
 * @param in the input stream reader
 */
public void setParameterXMLInput(InputStreamReader in)
    throws IOException, SAXException
{
    paramReader = new ParameterReader(this, in);
}

/**
 * Remembers an input source and reads parameter values from it later. To
 * speicfy a URL, use InputSource("http://...")</code>.
 *
 * @param in the input source
 */
public void setParameterXMLInput(InputSource in)
    throws IOException, SAXException
{
    paramReader = new ParameterReader(this, in);
}

/**
 * Reads an XML file and builds the contents of this report. Uses a
 * <code>ReportReader</code>.
 *
 * @param fileName a file name string
 * @see ReportReader#read(String)
 */
public void read(String fileName)
    throws FileNotFoundException, IOException, SAXException
{
    new ReportReader(this).read(fileName);
}

/**
 * The old name of <code>read(String)</code>. Use that instead.
 *
 * @param fileName the name of the XML file
 * @see #read(String)
 */
public void readFile(String fileName)
    throws FileNotFoundException, IOException, SAXException
{
    read(fileName);		// The old name
}

/**
 * Reads an XML stream using a <code>java.io.Reader</code> and builds the
 * contents of this report. Uses a <code>ReportReader</code>.
 *
 * @param in a reader
 * @see ReportReader#read(java.io.Reader)
 */
public void read(Reader in) throws IOException, SAXException {
    new ReportReader(this).read(in);
}

/**
 * Reads an XML stream using a <code>java.io.InputStreamReader</code> and
 * builds the contents of this report. Uses a <code>ReportReader</code>.
 *
 * @param in a reader
 * @see ReportReader#read(java.io.Reader)
 */
public void read(InputStreamReader in) throws IOException, SAXException {
    new ReportReader(this).read(in);
}

/**
 * Reads an XML stream using a <code>org.xml.sax.InputSource</code> and
 * builds the contents of this report. Uses a <code>ReportReader</code>.
 * To specify a URL, use <code>new InputSource("http://...")</code>.
 *
 * @param in a reader
 * @see ReportReader#read(org.xml.sax.InputSource)
 */
public void read(org.xml.sax.InputSource in) throws IOException, SAXException {
    new ReportReader(this).read(in);
}

/**
 * Writes the contents of this report as an XML file.
 *
 * @param fileName a file name string
 */
public void writeFile(String fileName) {
    XMLWriter out = null;
    try {
	out = new XMLWriter(new OutputStreamWriter(new FileOutputStream(fileName),
						      XML_JAVA_ENCODING));
	writeXML(out);
	out.close();
    }
    catch (IOException ioe) {
	ErrorHandler.error(I18N.get("Report.write_err") + ' ' + fileName, ioe,
			   I18N.get("Report.write_err_title"));
    }
    finally {
	if (out != null) out.close();
    }
}

/**
 * Writes the contents of this report as an XML file.
 *
 * @param out an indent writer
 */
public void writeXML(XMLWriter out) {
    writeXMLDecl(out);
    writeComment(out);
    writeReport(out);
}

protected void writeXMLDecl(XMLWriter out) {
    out.xmlDecl(XML_ENCODING_ATTRIBUTE);
}

protected void writeComment(XMLWriter out) {
    out.comment("Generated by DataVision version " + info.Version);
    out.comment(info.URL);
}

protected void writeReport(XMLWriter out) {
    out.startElement("report");
    out.attr("dtd-version", OUTPUT_DTD_VERSION);
    out.attr("name", name);
    out.attr("title", title);
    out.attr("author", author);

    writeDescription(out);
    writeStartFormula(out);
    paperFormat.writeXML(out);
    dataSource.writeXML(out);
    ListWriter.writeList(out, subreports.values(), "subreports");
    ListWriter.writeList(out, parameters.values(), "parameters");
    ListWriter.writeList(out, formulas.values(), "formulas");
    ListWriter.writeList(out, usercols.values(), "usercols");
    ListWriter.writeList(out, reportHeaders, "headers");
    ListWriter.writeList(out, reportFooters, "footers");
    writePage(out);
    ListWriter.writeList(out, groups, "groups");
    ListWriter.writeList(out, details, "details");

    out.endElement();
}

protected void writeDescription(XMLWriter out) {
    out.cdataElement("description", description);
}

protected void writeStartFormula(XMLWriter out) {
    if (startFormula != null)
	startFormula.writeXML(out);
}

protected void writePage(XMLWriter out) {
    out.startElement("page");
    ListWriter.writeList(out, pageHeaders, "headers");
    ListWriter.writeList(out, pageFooters, "footers");
    out.endElement();
}

/** Satec */
public void addFilterTable(String name){
    try{
        Table t= database.findTable(name);
        /** comprobamos que la tabla existe en BD */
        if (t != null){
            /** comprobamos que no este en la lista de tablas utilizadas en la plantilla */
            Iterator it= this.getDataSource().tablesUsedInReport();
            DatabaseMetaData dbmd = database.conn.getMetaData();
            boolean encontrado= false;

            if (it != null){
                for (;it.hasNext();) {
                    Table tabla = (Table)it.next();
                    if (tabla.getName().equalsIgnoreCase(name)) encontrado= true;

                }
                if (!encontrado){
                    SQLTable tabla = new SQLTable(database, name, dbmd);
                    filterTables.put(tabla.getId().toString(), tabla);

                    //System.out.println("[Report] Tabla annadida al filterTable TABLA.ID="+tabla.getId().toString());
                }else encontrado= false;
            }
        }
    }catch (Exception e){

    }
}
 /** Satec */
public Hashtable getFilterTables(){
    return filterTables;
}

public void setOracle(boolean b){
    this.isOracle= b;
}

public boolean isOracle(){
    return isOracle;
}

/**/

}
