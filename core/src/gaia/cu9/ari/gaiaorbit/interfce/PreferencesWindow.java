package gaia.cu9.ari.gaiaorbit.interfce;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import gaia.cu9.ari.gaiaorbit.event.Events;
import gaia.cu9.ari.gaiaorbit.event.IObserver;
import gaia.cu9.ari.gaiaorbit.interfce.KeyBindings.ProgramAction;
import gaia.cu9.ari.gaiaorbit.interfce.beans.ComboBoxBean;
import gaia.cu9.ari.gaiaorbit.interfce.beans.LangComboBoxBean;
import gaia.cu9.ari.gaiaorbit.util.Constants;
import gaia.cu9.ari.gaiaorbit.util.GlobalConf;
import gaia.cu9.ari.gaiaorbit.util.GlobalResources;
import gaia.cu9.ari.gaiaorbit.util.I18n;
import gaia.cu9.ari.gaiaorbit.util.format.INumberFormat;
import gaia.cu9.ari.gaiaorbit.util.format.NumberFormatFactory;
import gaia.cu9.ari.gaiaorbit.util.math.MathUtilsd;
import gaia.cu9.ari.gaiaorbit.util.scene2d.FileChooser;
import gaia.cu9.ari.gaiaorbit.util.scene2d.FileChooser.ResultListener;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnCheckBox;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnImageButton;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnLabel;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnScrollPane;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnSelectBox;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnSlider;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnTextArea;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnTextButton;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnTextField;
import gaia.cu9.ari.gaiaorbit.util.scene2d.OwnTextIconButton;
import gaia.cu9.ari.gaiaorbit.util.validator.IValidator;
import gaia.cu9.ari.gaiaorbit.util.validator.IntValidator;
import gaia.cu9.ari.gaiaorbit.util.validator.RegexpValidator;

public class PreferencesWindow extends GenericDialog implements IObserver {

    private LabelStyle linkStyle;

    private Array<Table> contents;
    private Array<OwnLabel> labels;

    private IValidator widthValidator, heightValidator, screenshotsSizeValidator, frameoutputSizeValidator;

    private INumberFormat nf3;

    public PreferencesWindow(Stage stage, Skin skin) {
        super(txt("gui.settings") + " - v" + GlobalConf.version.version + " - " + txt("gui.build", GlobalConf.version.build), skin, stage);
        this.linkStyle = skin.get("link", LabelStyle.class);

        this.contents = new Array<Table>();
        this.labels = new Array<OwnLabel>();

        this.nf3 = NumberFormatFactory.getFormatter("0.000");

        setAcceptText(txt("gui.saveprefs"));
        setCancelText(txt("gui.cancel"));

        // Build UI
        buildSuper();
    }

    @Override
    protected void build() {
        float contentw = 700 * GlobalConf.SCALE_FACTOR;
        float contenth = 700 * GlobalConf.SCALE_FACTOR;
        float tawidth = 400 * GlobalConf.SCALE_FACTOR;
        float tabwidth = 180 * GlobalConf.SCALE_FACTOR;
        float textwidth = 65 * GlobalConf.SCALE_FACTOR;
        float scrollw = 400 * GlobalConf.SCALE_FACTOR;
        float scrollh = 250 * GlobalConf.SCALE_FACTOR;

        // Create the tab buttons
        VerticalGroup group = new VerticalGroup();
        group.align(Align.left | Align.top);

        final Button tabGraphics = new OwnTextIconButton(txt("gui.graphicssettings"), new Image(skin.getDrawable("icon-p-graphics")), skin, "toggle-big");
        tabGraphics.pad(pad);
        tabGraphics.setWidth(tabwidth);
        final Button tabUI = new OwnTextIconButton(txt("gui.ui.interfacesettings"), new Image(skin.getDrawable("icon-p-interface")), skin, "toggle-big");
        tabUI.pad(pad);
        tabUI.setWidth(tabwidth);
        final Button tabPerformance = new OwnTextIconButton(txt("gui.multithreading"), new Image(skin.getDrawable("icon-p-performance")), skin, "toggle-big");
        tabPerformance.pad(pad);
        tabPerformance.setWidth(tabwidth);
        final Button tabControls = new OwnTextIconButton(txt("gui.controls"), new Image(skin.getDrawable("icon-p-controls")), skin, "toggle-big");
        tabControls.pad(pad);
        tabControls.setWidth(tabwidth);
        final Button tabScreenshots = new OwnTextIconButton(txt("gui.screenshots"), new Image(skin.getDrawable("icon-p-screenshots")), skin, "toggle-big");
        tabScreenshots.pad(pad);
        tabScreenshots.setWidth(tabwidth);
        final Button tabFrames = new OwnTextIconButton(txt("gui.frameoutput.title"), new Image(skin.getDrawable("icon-p-frameoutput")), skin, "toggle-big");
        tabFrames.pad(pad);
        tabFrames.setWidth(tabwidth);
        final Button tabCamera = new OwnTextIconButton(txt("gui.camerarec.title"), new Image(skin.getDrawable("icon-p-camera")), skin, "toggle-big");
        tabCamera.pad(pad);
        tabCamera.setWidth(tabwidth);
        final Button tab360 = new OwnTextIconButton(txt("gui.360.title"), new Image(skin.getDrawable("icon-p-360")), skin, "toggle-big");
        tab360.pad(pad);
        tab360.setWidth(tabwidth);
        final Button tabData = new OwnTextIconButton(txt("gui.data"), new Image(skin.getDrawable("icon-p-data")), skin, "toggle-big");
        tabData.pad(pad);
        tabData.setWidth(tabwidth);
        final Button tabGaia = new OwnTextIconButton(txt("gui.gaia"), new Image(skin.getDrawable("icon-p-gaia")), skin, "toggle-big");
        tabGaia.pad(pad);
        tabGaia.setWidth(tabwidth);

        group.addActor(tabGraphics);
        group.addActor(tabUI);
        group.addActor(tabPerformance);
        group.addActor(tabControls);
        group.addActor(tabScreenshots);
        group.addActor(tabFrames);
        group.addActor(tabCamera);
        group.addActor(tab360);
        group.addActor(tabData);
        group.addActor(tabGaia);
        content.add(group).align(Align.left | Align.top).padLeft(pad);

        // Create the tab content. Just using images here for simplicity.
        Stack tabContent = new Stack();
        tabContent.setSize(contentw, contenth);

        /**
         *  ==== GRAPHICS ====
         **/
        final Table contentGraphics = new Table(skin);
        contents.add(contentGraphics);
        contentGraphics.align(Align.top | Align.left);

        // RESOLUTION/MODE
        Label titleResolution = new OwnLabel(txt("gui.resolutionmode"), skin, "help-title");
        Table mode = new Table();

        // Full screen mode resolutions
        Array<DisplayMode> modes = new Array<DisplayMode>(Gdx.graphics.getDisplayModes());
        modes.sort(new Comparator<DisplayMode>() {

            @Override
            public int compare(DisplayMode o1, DisplayMode o2) {
                return Integer.compare(o2.height * o2.width, o1.height * o1.width);
            }
        });
        final OwnSelectBox<DisplayMode> fullscreenResolutions = new OwnSelectBox<DisplayMode>(skin);
        fullscreenResolutions.setWidth(textwidth * 3.3f);
        fullscreenResolutions.setItems(modes);

        DisplayMode selectedMode = null;
        for (DisplayMode dm : modes) {
            if (dm.width == GlobalConf.screen.FULLSCREEN_WIDTH && dm.height == GlobalConf.screen.FULLSCREEN_HEIGHT) {
                selectedMode = dm;
                break;
            }
        }
        if (selectedMode != null)
            fullscreenResolutions.setSelected(selectedMode);

        // Get current resolution
        Table windowedResolutions = new Table(skin);
        DisplayMode nativeMode = Gdx.graphics.getDisplayMode();
        widthValidator = new IntValidator(100, nativeMode.width);
        final OwnTextField widthField = new OwnTextField(Integer.toString(MathUtils.clamp(GlobalConf.screen.SCREEN_WIDTH, 100, nativeMode.width)), skin, widthValidator);
        widthField.setWidth(textwidth);
        heightValidator = new IntValidator(100, nativeMode.height);
        final OwnTextField heightField = new OwnTextField(Integer.toString(MathUtils.clamp(GlobalConf.screen.SCREEN_HEIGHT, 100, nativeMode.height)), skin, heightValidator);
        heightField.setWidth(textwidth);
        final OwnCheckBox resizable = new OwnCheckBox(txt("gui.resizable"), skin, "default", pad);
        resizable.setChecked(GlobalConf.screen.RESIZABLE);
        final OwnLabel widthLabel = new OwnLabel(txt("gui.width") + ":", skin);
        final OwnLabel heightLabel = new OwnLabel(txt("gui.height") + ":", skin);

        windowedResolutions.add(widthLabel).left().padRight(pad);
        windowedResolutions.add(widthField).left().padRight(pad);
        windowedResolutions.add(heightLabel).left().padRight(pad);
        windowedResolutions.add(heightField).left().row();
        windowedResolutions.add(resizable).left().colspan(4);

        // Radio buttons
        final OwnCheckBox fullscreen = new OwnCheckBox(txt("gui.fullscreen"), skin, "radio", pad);
        fullscreen.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    GlobalConf.screen.FULLSCREEN = fullscreen.isChecked();
                    selectFullscreen(fullscreen.isChecked(), widthField, heightField, fullscreenResolutions, resizable, widthLabel, heightLabel);
                    return true;
                }
                return false;
            }
        });
        fullscreen.setChecked(GlobalConf.screen.FULLSCREEN);

        final OwnCheckBox windowed = new OwnCheckBox(txt("gui.windowed"), skin, "radio", pad);
        windowed.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    GlobalConf.screen.FULLSCREEN = !windowed.isChecked();
                    selectFullscreen(!windowed.isChecked(), widthField, heightField, fullscreenResolutions, resizable, widthLabel, heightLabel);
                    return true;
                }
                return false;
            }
        });
        windowed.setChecked(!GlobalConf.screen.FULLSCREEN);
        selectFullscreen(GlobalConf.screen.FULLSCREEN, widthField, heightField, fullscreenResolutions, resizable, widthLabel, heightLabel);

        new ButtonGroup<CheckBox>(fullscreen, windowed);

        mode.add(fullscreen).left().padRight(pad * 2);
        mode.add(fullscreenResolutions).left().row();
        mode.add(windowed).left().padRight(pad * 2).padTop(pad * 2);
        mode.add(windowedResolutions).left().padTop(pad * 2);

        // Add to content
        contentGraphics.add(titleResolution).left().padBottom(pad * 2).row();
        contentGraphics.add(mode).left().padBottom(pad * 4).row();

        // GRAPHICS SETTINGS
        Label titleGraphics = new OwnLabel(txt("gui.graphicssettings"), skin, "help-title");
        Table graphics = new Table();

        OwnLabel gqualityLabel = new OwnLabel(txt("gui.gquality"), skin);
        gqualityLabel.addListener(new TextTooltip(txt("gui.gquality.info"), skin));

        ComboBoxBean[] gqs = new ComboBoxBean[] { new ComboBoxBean(txt("gui.gquality.high"), 0), new ComboBoxBean(txt("gui.gquality.normal"), 1), new ComboBoxBean(txt("gui.gquality.low"), 2) };
        OwnSelectBox<ComboBoxBean> gquality = new OwnSelectBox<ComboBoxBean>(skin);
        gquality.setItems(gqs);
        gquality.setWidth(textwidth * 3f);
        int index = -1;
        for (int i = 0; i < GlobalConf.data.OBJECTS_JSON_FILE_GQ.length; i++) {
            if (GlobalConf.data.OBJECTS_JSON_FILE_GQ[i].equals(GlobalConf.data.OBJECTS_JSON_FILE)) {
                index = i;
                break;
            }
        }
        int gqidx = index;
        gquality.setSelected(gqs[gqidx]);

        OwnImageButton gqualityTooltip = new OwnImageButton(skin, "tooltip");
        gqualityTooltip.addListener(new TextTooltip(txt("gui.gquality.info"), skin));

        // AA
        OwnLabel aaLabel = new OwnLabel(txt("gui.aa"), skin);
        aaLabel.addListener(new TextTooltip(txt("gui.aa.info"), skin));

        ComboBoxBean[] aas = new ComboBoxBean[] { new ComboBoxBean(txt("gui.aa.no"), 0), new ComboBoxBean(txt("gui.aa.fxaa"), -1), new ComboBoxBean(txt("gui.aa.nfaa"), -2) };
        OwnSelectBox<ComboBoxBean> aa = new OwnSelectBox<ComboBoxBean>(skin);
        aa.setItems(aas);
        aa.setWidth(textwidth * 3f);
        aa.setSelected(aas[idxAa(2, GlobalConf.postprocess.POSTPROCESS_ANTIALIAS)]);

        OwnImageButton aaTooltip = new OwnImageButton(skin, "tooltip");
        aaTooltip.addListener(new TextTooltip(txt("gui.aa.info"), skin));

        // LINE RENDERER
        OwnLabel lrLabel = new OwnLabel(txt("gui.linerenderer"), skin);
        ComboBoxBean[] lineRenderers = new ComboBoxBean[] { new ComboBoxBean(txt("gui.linerenderer.normal"), 0), new ComboBoxBean(txt("gui.linerenderer.quad"), 1) };
        OwnSelectBox<ComboBoxBean> lineRenderer = new OwnSelectBox<ComboBoxBean>(skin);
        lineRenderer.setItems(lineRenderers);
        lineRenderer.setWidth(textwidth * 3f);
        lineRenderer.setSelected(lineRenderers[GlobalConf.scene.LINE_RENDERER]);

        // VSYNC
        OwnCheckBox vsync = new OwnCheckBox(txt("gui.vsync"), skin, "default", pad);
        vsync.setChecked(GlobalConf.screen.VSYNC);

        // LABELS
        labels.addAll(gqualityLabel, aaLabel, lrLabel);

        graphics.add(gqualityLabel).left().padRight(pad * 4).padBottom(pad);
        graphics.add(gquality).left().padRight(pad * 2).padBottom(pad);
        graphics.add(gqualityTooltip).left().padBottom(pad).row();
        graphics.add(aaLabel).left().padRight(pad * 4).padBottom(pad);
        graphics.add(aa).left().padRight(pad * 2).padBottom(pad);
        graphics.add(aaTooltip).left().padBottom(pad).row();
        graphics.add(lrLabel).left().padRight(pad * 4).padBottom(pad);
        graphics.add(lineRenderer).colspan(2).left().padBottom(pad).row();
        graphics.add(vsync).left().colspan(3);

        // Add to content
        contentGraphics.add(titleGraphics).left().padBottom(pad * 2).row();
        contentGraphics.add(graphics).left();

        /**
         *  ==== UI ====
         **/
        final Table contentUI = new Table(skin);
        contents.add(contentUI);
        contentUI.align(Align.top | Align.left);

        OwnLabel titleUI = new OwnLabel(txt("gui.ui.interfacesettings"), skin, "help-title");

        Table ui = new Table();

        // LANGUAGE
        OwnLabel langLabel = new OwnLabel(txt("gui.ui.language"), skin);
        File i18nfolder = new File(System.getProperty("assets.location") + "i18n/");
        String i18nname = "gsbundle";
        String[] files = i18nfolder.list();
        LangComboBoxBean[] langs = new LangComboBoxBean[files.length];
        int i = 0;
        for (String file : files) {
            if (file.startsWith("gsbundle") && file.endsWith(".properties")) {
                String locale = file.substring(i18nname.length(), file.length() - ".properties".length());
                if (locale.length() != 0) {
                    // Remove underscore _
                    locale = locale.substring(1).replace("_", "-");
                    Locale loc = Locale.forLanguageTag(locale);
                    langs[i] = new LangComboBoxBean(loc);
                } else {
                    langs[i] = new LangComboBoxBean(I18n.bundle.getLocale());
                }
            }
            i++;
        }
        Arrays.sort(langs);

        OwnSelectBox<LangComboBoxBean> lang = new OwnSelectBox<LangComboBoxBean>(skin);
        lang.setWidth(textwidth * 3f);
        lang.setItems(langs);
        lang.setSelected(langs[idxLang(GlobalConf.program.LOCALE, langs)]);

        // THEME
        OwnLabel themeLabel = new OwnLabel(txt("gui.ui.theme"), skin);
        String[] themes = new String[] { "dark-green", "dark-green-x2", "dark-blue", "dark-blue-x2", "dark-orange", "dark-orange-x2", "bright-green", "bright-green-x2" };
        OwnSelectBox<String> theme = new OwnSelectBox<String>(skin);
        theme.setWidth(textwidth * 3f);
        theme.setItems(themes);
        theme.setSelected(GlobalConf.program.UI_THEME);

        // LABELS
        labels.addAll(langLabel, themeLabel);

        // Add to table
        ui.add(langLabel).left().padRight(pad * 4).padBottom(pad);
        ui.add(lang).left().padBottom(pad).row();
        ui.add(themeLabel).left().padRight(pad * 4).padBottom(pad);
        ui.add(theme).left().padBottom(pad).row();

        // Add to content
        contentUI.add(titleUI).left().padBottom(pad * 2).row();
        contentUI.add(ui).left();

        /**
         *  ==== PERFORMANCE ====
         **/
        final Table contentPerformance = new Table(skin);
        contents.add(contentPerformance);
        contentPerformance.align(Align.top | Align.left);

        // MULTITHREADING
        OwnLabel titleMultithread = new OwnLabel(txt("gui.multithreading"), skin, "help-title");

        Table multithread = new Table(skin);

        OwnLabel numThreadsLabel = new OwnLabel(txt("gui.thread.number"), skin);
        int maxthreads = Runtime.getRuntime().availableProcessors();
        ComboBoxBean[] cbs = new ComboBoxBean[maxthreads + 1];
        cbs[0] = new ComboBoxBean(txt("gui.letdecide"), 0);
        for (i = 1; i <= maxthreads; i++) {
            cbs[i] = new ComboBoxBean(txt("gui.thread", i), i);
        }
        final OwnSelectBox<ComboBoxBean> numThreads = new OwnSelectBox<ComboBoxBean>(skin);
        numThreads.setWidth(textwidth * 3f);
        numThreads.setItems(cbs);
        numThreads.setSelectedIndex(GlobalConf.performance.NUMBER_THREADS);

        final OwnCheckBox multithreadCb = new OwnCheckBox(txt("gui.thread.enable"), skin, "default", pad);
        multithreadCb.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    numThreads.setDisabled(!multithreadCb.isChecked());
                    return true;
                }
                return false;
            }
        });
        multithreadCb.setChecked(GlobalConf.performance.MULTITHREADING);
        numThreads.setDisabled(!multithreadCb.isChecked());

        // Add to table
        multithread.add(multithreadCb).colspan(2).left().padBottom(pad).row();
        multithread.add(numThreadsLabel).left().padRight(pad * 4).padBottom(pad);
        multithread.add(numThreads).left().padBottom(pad).row();

        // Add to content
        contentPerformance.add(titleMultithread).left().padBottom(pad * 2).row();
        contentPerformance.add(multithread).left().padBottom(pad * 4).row();

        // DRAW DISTANCE
        OwnLabel titleLod = new OwnLabel(txt("gui.lod"), skin, "help-title");

        Table lod = new Table(skin);

        // Smooth transitions
        final OwnCheckBox lodFadeCb = new OwnCheckBox(txt("gui.lod.fade"), skin, "default", pad);
        lodFadeCb.setChecked(GlobalConf.scene.OCTREE_PARTICLE_FADE);

        // Draw distance
        OwnLabel ddLabel = new OwnLabel(txt("gui.lod.thresholds"), skin);
        final OwnSlider lodTransitions = new OwnSlider(Constants.MIN_SLIDER, Constants.MAX_SLIDER, 0.1f, false, skin);
        lodTransitions.setValue(Math.round(MathUtilsd.lint(GlobalConf.scene.OCTANT_THRESHOLD_0, Constants.MIN_LOD_TRANS_ANGLE, Constants.MAX_LOD_TRANS_ANGLE, Constants.MIN_SLIDER, Constants.MAX_SLIDER)));

        final OwnLabel lodValueLabel = new OwnLabel(nf3.format(GlobalConf.scene.OCTANT_THRESHOLD_0), skin);

        lodTransitions.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    OwnSlider slider = (OwnSlider) event.getListenerActor();
                    lodValueLabel.setText(nf3.format(MathUtilsd.lint(slider.getValue(), Constants.MIN_SLIDER, Constants.MAX_SLIDER, Constants.MIN_LOD_TRANS_ANGLE, Constants.MAX_LOD_TRANS_ANGLE)));
                    return true;
                }
                return false;
            }
        });

        OwnImageButton lodTooltip = new OwnImageButton(skin, "tooltip");
        lodTooltip.addListener(new TextTooltip(txt("gui.lod.thresholds.info"), skin));

        // LABELS
        labels.addAll(numThreadsLabel, ddLabel);

        // Add to table
        lod.add(lodFadeCb).colspan(4).left().padBottom(pad).row();
        lod.add(ddLabel).left().padRight(pad * 4).padBottom(pad);
        lod.add(lodTransitions).left().padRight(pad * 4).padBottom(pad);
        lod.add(lodValueLabel).left().padRight(pad * 4).padBottom(pad);
        lod.add(lodTooltip).left().padBottom(pad);

        // Add to content
        contentPerformance.add(titleLod).left().padBottom(pad * 2).row();
        contentPerformance.add(lod).left();

        /**
         *  ==== CONTROLS ====
         **/
        final Table contentControls = new Table(skin);
        contents.add(contentControls);
        contentControls.align(Align.top | Align.left);

        // KEY BINDINGS
        OwnLabel titleKeybindings = new OwnLabel(txt("gui.keymappings"), skin, "help-title");

        Map<TreeSet<Integer>, ProgramAction> maps = KeyBindings.instance.getSortedMappings();
        Set<TreeSet<Integer>> keymaps = maps.keySet();
        String[][] data = new String[maps.size()][2];

        i = 0;
        for (TreeSet<Integer> keys : keymaps) {
            ProgramAction action = maps.get(keys);
            data[i][0] = action.actionName;
            data[i][1] = keysToString(keys);
            i++;
        }

        Table controls = new Table(skin);
        controls.align(Align.left | Align.top);
        // Header
        controls.add(new OwnLabel(txt("gui.keymappings.action"), skin, "header")).left();
        controls.add(new OwnLabel(txt("gui.keymappings.keys"), skin, "header")).left().row();

        // Controls
        for (String[] pair : data) {
            controls.add(new OwnLabel(pair[0], skin)).left();
            controls.add(new OwnLabel(pair[1], skin)).left().row();
        }

        OwnScrollPane controlsScroll = new OwnScrollPane(controls, skin, "default-nobg");
        controlsScroll.setWidth(scrollw);
        controlsScroll.setHeight(scrollh);
        controlsScroll.setForceScroll(false, true);
        controlsScroll.setSmoothScrolling(true);
        controlsScroll.setFadeScrollBars(false);
        scrolls.add(controlsScroll);

        // Add to content
        contentControls.add(titleKeybindings).left().padBottom(pad * 2).row();
        contentControls.add(controlsScroll).left();

        /**
         *  ==== SCREENSHOTS ====
         **/
        final Table contentScreenshots = new Table(skin);
        contents.add(contentScreenshots);
        contentScreenshots.align(Align.top | Align.left);

        // SCREEN CAPTURE
        OwnLabel titleScreenshots = new OwnLabel(txt("gui.screencapture"), skin, "help-title");

        Table screenshots = new Table(skin);

        // Info
        String ssinfostr = txt("gui.screencapture.info") + '\n';
        int lines = GlobalResources.countOccurrences(ssinfostr, '\n');
        TextArea screenshotsInfo = new OwnTextArea(ssinfostr, skin, "info");
        screenshotsInfo.setDisabled(true);
        screenshotsInfo.setPrefRows(lines + 1);
        screenshotsInfo.setWidth(tawidth);
        screenshotsInfo.clearListeners();

        // Save location
        OwnLabel screenshotsLocationLabel = new OwnLabel(txt("gui.screenshots.save"), skin);
        screenshotsLocationLabel.pack();
        final OwnTextButton screenshotsLocation = new OwnTextButton(GlobalConf.screenshot.SCREENSHOT_FOLDER, skin);
        screenshotsLocation.pad(pad);
        screenshotsLocation.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    FileChooser fc = FileChooser.createPickDialog(txt("gui.screenshots.directory.choose"), skin, Gdx.files.absolute(GlobalConf.screenshot.SCREENSHOT_FOLDER));
                    fc.setResultListener(new ResultListener() {
                        @Override
                        public boolean result(boolean success, FileHandle result) {
                            if (success) {
                                // do stuff with result
                                screenshotsLocation.setText(result.path());
                            }
                            return true;
                        }
                    });
                    fc.setOkButtonText(txt("gui.ok"));
                    fc.setCancelButtonText(txt("gui.cancel"));
                    fc.setFilter(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isDirectory();
                        }
                    });
                    fc.show(stage);

                    return true;
                }
                return false;
            }
        });

        // Size
        final OwnLabel screenshotsSizeLabel = new OwnLabel(txt("gui.screenshots.size"), skin);
        final OwnLabel xLabel = new OwnLabel("x", skin);
        screenshotsSizeValidator = new IntValidator(GlobalConf.ScreenshotConf.MIN_SCREENSHOT_SIZE, GlobalConf.ScreenshotConf.MAX_SCREENSHOT_SIZE);
        final OwnTextField sswidthField = new OwnTextField(Integer.toString(MathUtils.clamp(GlobalConf.screenshot.SCREENSHOT_WIDTH, GlobalConf.ScreenshotConf.MIN_SCREENSHOT_SIZE, GlobalConf.ScreenshotConf.MAX_SCREENSHOT_SIZE)), skin, screenshotsSizeValidator);
        sswidthField.setWidth(textwidth);
        final OwnTextField ssheightField = new OwnTextField(Integer.toString(MathUtils.clamp(GlobalConf.screenshot.SCREENSHOT_HEIGHT, GlobalConf.ScreenshotConf.MIN_SCREENSHOT_SIZE, GlobalConf.ScreenshotConf.MAX_SCREENSHOT_SIZE)), skin, screenshotsSizeValidator);
        ssheightField.setWidth(textwidth);
        HorizontalGroup ssSizeGroup = new HorizontalGroup();
        ssSizeGroup.space(pad * 2);
        ssSizeGroup.addActor(sswidthField);
        ssSizeGroup.addActor(xLabel);
        ssSizeGroup.addActor(ssheightField);

        // Mode
        OwnLabel ssModeLabel = new OwnLabel(txt("gui.screenshots.mode"), skin);
        ComboBoxBean[] screenshotModes = new ComboBoxBean[] { new ComboBoxBean(txt("gui.screenshots.mode.simple"), 0), new ComboBoxBean(txt("gui.screenshots.mode.redraw"), 1) };
        final OwnSelectBox<ComboBoxBean> screenshotMode = new OwnSelectBox<ComboBoxBean>(skin);
        screenshotMode.setItems(screenshotModes);
        screenshotMode.setWidth(textwidth * 3f);
        screenshotMode.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    if (screenshotMode.getSelected().value == 0) {
                        // Simple
                        enableComponents(false, sswidthField, ssheightField, screenshotsSizeLabel, xLabel);
                    } else {
                        // Redraw
                        enableComponents(true, sswidthField, ssheightField, screenshotsSizeLabel, xLabel);
                    }
                    return true;
                }
                return false;
            }
        });
        screenshotMode.setSelected(screenshotModes[GlobalConf.screenshot.SCREENSHOT_MODE.ordinal()]);

        OwnImageButton screenshotsModeTooltip = new OwnImageButton(skin, "tooltip");
        screenshotsModeTooltip.addListener(new TextTooltip(txt("gui.tooltip.screenshotmode"), skin));

        HorizontalGroup ssModeGroup = new HorizontalGroup();
        ssModeGroup.space(pad);
        ssModeGroup.addActor(screenshotMode);
        ssModeGroup.addActor(screenshotsModeTooltip);

        // LABELS
        labels.addAll(screenshotsLocationLabel, ssModeLabel, screenshotsSizeLabel);

        // Add to table
        screenshots.add(screenshotsInfo).colspan(2).left().padBottom(pad).row();
        screenshots.add(screenshotsLocationLabel).left().padRight(pad * 4).padBottom(pad);
        screenshots.add(screenshotsLocation).left().expandX().padBottom(pad).row();
        screenshots.add(ssModeLabel).left().padRight(pad * 4).padBottom(pad);
        screenshots.add(ssModeGroup).left().expandX().padBottom(pad).row();
        screenshots.add(screenshotsSizeLabel).left().padRight(pad * 4).padBottom(pad);
        screenshots.add(ssSizeGroup).left().expandX().padBottom(pad).row();

        // Add to content
        contentScreenshots.add(titleScreenshots).left().padBottom(pad * 2).row();
        contentScreenshots.add(screenshots).left();

        /**
         *  ==== FRAME OUTPUT ====
         **/
        final Table contentFrames = new Table(skin);
        contents.add(contentFrames);
        contentFrames.align(Align.top | Align.left);

        // FRAME OUTPUT CONFIG
        OwnLabel titleFrameoutput = new OwnLabel(txt("gui.frameoutput"), skin, "help-title");

        Table frameoutput = new Table(skin);

        // Info
        String foinfostr = txt("gui.frameoutput.info") + '\n';
        lines = GlobalResources.countOccurrences(foinfostr, '\n');
        TextArea frameoutputInfo = new OwnTextArea(foinfostr, skin, "info");
        frameoutputInfo.setDisabled(true);
        frameoutputInfo.setPrefRows(lines + 1);
        frameoutputInfo.setWidth(tawidth);
        frameoutputInfo.clearListeners();

        // Save location
        OwnLabel frameoutputLocationLabel = new OwnLabel(txt("gui.frameoutput.location"), skin);
        final OwnTextButton frameoutputLocation = new OwnTextButton(GlobalConf.frame.RENDER_FOLDER, skin);
        frameoutputLocation.pad(pad);
        frameoutputLocation.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    FileChooser fc = FileChooser.createPickDialog(txt("gui.frameoutput.directory.choose"), skin, Gdx.files.absolute(GlobalConf.frame.RENDER_FOLDER));
                    fc.setResultListener(new ResultListener() {
                        @Override
                        public boolean result(boolean success, FileHandle result) {
                            if (success) {
                                // do stuff with result
                                frameoutputLocation.setText(result.path());
                            }
                            return true;
                        }
                    });
                    fc.setOkButtonText(txt("gui.ok"));
                    fc.setCancelButtonText(txt("gui.cancel"));
                    fc.setFilter(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isDirectory();
                        }
                    });
                    fc.show(stage);

                    return true;
                }
                return false;
            }
        });

        // Prefix
        OwnLabel prefixLabel = new OwnLabel(txt("gui.frameoutput.prefix"), skin);
        OwnTextField frameoutputPrefix = new OwnTextField(GlobalConf.frame.RENDER_FILE_NAME, skin, new RegexpValidator("^\\w+$"));
        frameoutputPrefix.setWidth(textwidth * 3f);

        // FPS
        OwnLabel fpsLabel = new OwnLabel(txt("gui.frameoutput.fps"), skin);
        OwnTextField frameoutputFps = new OwnTextField(Integer.toString(GlobalConf.frame.RENDER_TARGET_FPS), skin, new IntValidator(1, 200));
        frameoutputFps.setWidth(textwidth * 3f);

        // Size
        final OwnLabel frameoutputSizeLabel = new OwnLabel(txt("gui.frameoutput.size"), skin);
        final OwnLabel xLabelfo = new OwnLabel("x", skin);
        frameoutputSizeValidator = new IntValidator(GlobalConf.FrameConf.MIN_FRAME_SIZE, GlobalConf.FrameConf.MAX_FRAME_SIZE);
        final OwnTextField fowidthField = new OwnTextField(Integer.toString(MathUtils.clamp(GlobalConf.frame.RENDER_WIDTH, GlobalConf.FrameConf.MIN_FRAME_SIZE, GlobalConf.FrameConf.MAX_FRAME_SIZE)), skin, frameoutputSizeValidator);
        fowidthField.setWidth(textwidth);
        final OwnTextField foheightField = new OwnTextField(Integer.toString(MathUtils.clamp(GlobalConf.frame.RENDER_HEIGHT, GlobalConf.FrameConf.MIN_FRAME_SIZE, GlobalConf.FrameConf.MAX_FRAME_SIZE)), skin, frameoutputSizeValidator);
        foheightField.setWidth(textwidth);
        HorizontalGroup foSizeGroup = new HorizontalGroup();
        foSizeGroup.space(pad * 2);
        foSizeGroup.addActor(fowidthField);
        foSizeGroup.addActor(xLabelfo);
        foSizeGroup.addActor(foheightField);

        // Mode
        OwnLabel fomodeLabel = new OwnLabel(txt("gui.screenshots.mode"), skin);
        ComboBoxBean[] frameoutputModes = new ComboBoxBean[] { new ComboBoxBean(txt("gui.screenshots.mode.simple"), 0), new ComboBoxBean(txt("gui.screenshots.mode.redraw"), 1) };
        final OwnSelectBox<ComboBoxBean> frameoutputMode = new OwnSelectBox<ComboBoxBean>(skin);
        frameoutputMode.setItems(frameoutputModes);
        frameoutputMode.setWidth(textwidth * 3f);
        frameoutputMode.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeEvent) {
                    if (frameoutputMode.getSelected().value == 0) {
                        // Simple
                        enableComponents(false, fowidthField, foheightField, frameoutputSizeLabel, xLabelfo);
                    } else {
                        // Redraw
                        enableComponents(true, fowidthField, foheightField, frameoutputSizeLabel, xLabelfo);
                    }
                    return true;
                }
                return false;
            }
        });
        frameoutputMode.setSelected(frameoutputModes[GlobalConf.frame.FRAME_MODE.ordinal()]);

        OwnImageButton frameoutputModeTooltip = new OwnImageButton(skin, "tooltip");
        frameoutputModeTooltip.addListener(new TextTooltip(txt("gui.tooltip.screenshotmode"), skin));

        HorizontalGroup foModeGroup = new HorizontalGroup();
        foModeGroup.space(pad);
        foModeGroup.addActor(frameoutputMode);
        foModeGroup.addActor(frameoutputModeTooltip);

        // LABELS
        labels.addAll(frameoutputLocationLabel, prefixLabel, fpsLabel, fomodeLabel, frameoutputSizeLabel);

        // Add to table
        frameoutput.add(frameoutputInfo).colspan(2).left().padBottom(pad).row();
        frameoutput.add(frameoutputLocationLabel).left().padRight(pad * 4).padBottom(pad);
        frameoutput.add(frameoutputLocation).left().expandX().padBottom(pad).row();
        frameoutput.add(prefixLabel).left().padRight(pad * 4).padBottom(pad);
        frameoutput.add(frameoutputPrefix).left().padBottom(pad).row();
        frameoutput.add(fpsLabel).left().padRight(pad * 4).padBottom(pad);
        frameoutput.add(frameoutputFps).left().padBottom(pad).row();
        frameoutput.add(fomodeLabel).left().padRight(pad * 4).padBottom(pad);
        frameoutput.add(foModeGroup).left().expandX().padBottom(pad).row();
        frameoutput.add(frameoutputSizeLabel).left().padRight(pad * 4).padBottom(pad);
        frameoutput.add(foSizeGroup).left().expandX().padBottom(pad).row();

        // Add to content
        contentFrames.add(titleFrameoutput).left().padBottom(pad * 2).row();
        contentFrames.add(frameoutput).left();

        /**
         *  ==== CAMERA ====
         **/
        final Table contentCamera = new Table(skin);
        contents.add(contentCamera);
        contentCamera.align(Align.top | Align.left);

        // CAMERA RECORDING
        Table camrec = new Table(skin);

        OwnLabel titleCamrec = new OwnLabel(txt("gui.camerarec.title"), skin, "help-title");

        // fps
        OwnLabel camfpsLabel = new OwnLabel(txt("gui.camerarec.fps"), skin);
        OwnTextField camrecFps = new OwnTextField(Integer.toString(GlobalConf.frame.CAMERA_REC_TARGET_FPS), skin, new IntValidator(1, 200));
        camrecFps.setWidth(textwidth * 3f);

        // Activate automatically
        CheckBox cbAutoCamrec = new OwnCheckBox(txt("gui.camerarec.frameoutput"), skin, "default", pad);
        cbAutoCamrec.setChecked(GlobalConf.frame.AUTO_FRAME_OUTPUT_CAMERA_PLAY);
        OwnImageButton camrecTooltip = new OwnImageButton(skin, "tooltip");
        camrecTooltip.addListener(new TextTooltip(txt("gui.tooltip.playcamera.frameoutput"), skin));

        HorizontalGroup cbGroup = new HorizontalGroup();
        cbGroup.space(pad);
        cbGroup.addActor(cbAutoCamrec);
        cbGroup.addActor(camrecTooltip);

        // LABELS
        labels.add(camfpsLabel);

        // Add to table
        camrec.add(camfpsLabel).left().padRight(pad * 4).padBottom(pad);
        camrec.add(camrecFps).left().expandX().padBottom(pad).row();
        camrec.add(cbGroup).colspan(2).left().padBottom(pad).row();

        // Add to content
        contentCamera.add(titleCamrec).left().padBottom(pad * 2).row();
        contentCamera.add(camrec).left();

        /**
         *  ==== 360 ====
         **/
        final Table content360 = new Table(skin);
        contents.add(content360);
        content360.align(Align.top | Align.left);

        // CUBEMAP
        OwnLabel titleCubemap = new OwnLabel(txt("gui.360"), skin, "help-title");
        Table cubemap = new Table(skin);

        // Info
        String cminfostr = txt("gui.360.info") + '\n';
        lines = GlobalResources.countOccurrences(cminfostr, '\n');
        TextArea cmInfo = new OwnTextArea(cminfostr, skin, "info");
        cmInfo.setDisabled(true);
        cmInfo.setPrefRows(lines + 1);
        cmInfo.setWidth(tawidth);
        cmInfo.clearListeners();

        // Resolution
        OwnLabel cmResolutionLabel = new OwnLabel(txt("gui.360.resolution"), skin);
        OwnTextField cmResolution = new OwnTextField(Integer.toString(GlobalConf.scene.CUBEMAP_FACE_RESOLUTION), skin, new IntValidator(20, 15000));
        cmResolution.setWidth(textwidth * 3f);

        // LABELS
        labels.add(cmResolutionLabel);

        // Add to table
        cubemap.add(cmInfo).colspan(2).left().padBottom(pad).row();
        cubemap.add(cmResolutionLabel).left().padRight(pad * 4).padBottom(pad);
        cubemap.add(cmResolution).left().expandX().padBottom(pad).row();

        // Add to content
        content360.add(titleCubemap).left().padBottom(pad * 2).row();
        content360.add(cubemap).left();

        /**
         *  ==== DATA ====
         **/
        final Table contentData = new Table(skin);
        contents.add(contentData);
        contentData.align(Align.top | Align.left);

        // DATA SOURCE
        OwnLabel titleData = new OwnLabel(txt("gui.data.source"), skin, "help-title");
        Table datasource = new Table(skin);

        CheckBox hyg = new OwnCheckBox(txt("gui.data.hyg"), skin, "radio", pad);
        hyg.setChecked(GlobalConf.data.CATALOG_JSON_FILE.equals(GlobalConf.data.HYG_JSON_FILE));
        CheckBox tgas = new OwnCheckBox(txt("gui.data.tgas"), skin, "radio", pad);
        tgas.setChecked(GlobalConf.data.CATALOG_JSON_FILE.equals(GlobalConf.data.TGAS_JSON_FILE));
        CheckBox tgasMultifile = new OwnCheckBox(txt("gui.data.tgas") + " multifile", skin, "radio", pad);
        tgasMultifile.setChecked(GlobalConf.data.CATALOG_JSON_FILE.equals("data/catalog-tgas-octree-multifile.json"));

        new ButtonGroup<CheckBox>(hyg, tgas, tgasMultifile);

        // Add to table
        datasource.add(hyg).left().padBottom(pad).row();
        datasource.add(tgas).left().padBottom(pad).row();
        datasource.add(tgasMultifile).left().padBottom(pad);

        // Add to content
        contentData.add(titleData).left().padBottom(pad * 2).row();
        contentData.add(datasource).left();

        /**
         *  ==== GAIA ====
         **/
        final Table contentGaia = new Table(skin);
        contents.add(contentGaia);
        contentGaia.align(Align.top | Align.left);

        // ATTITUDE
        OwnLabel titleAttitude = new OwnLabel(txt("gui.gaia.attitude"), skin, "help-title");
        Table attitude = new Table(skin);

        CheckBox real = new OwnCheckBox(txt("gui.gaia.real"), skin, "radio", pad);
        real.setChecked(GlobalConf.data.REAL_GAIA_ATTITUDE);
        CheckBox nsl = new OwnCheckBox(txt("gui.gaia.nsl"), skin, "radio", pad);
        nsl.setChecked(!GlobalConf.data.REAL_GAIA_ATTITUDE);

        new ButtonGroup<CheckBox>(real, nsl);

        // Add to table
        attitude.add(nsl).left().padBottom(pad).row();
        attitude.add(real).left().padBottom(pad);

        // Add to content
        contentGaia.add(titleAttitude).left().padBottom(pad * 2).row();
        contentGaia.add(attitude).left();

        /** COMPUTE LABEL WIDTH **/
        float maxLabelWidth = 0;
        for (OwnLabel l : labels) {
            l.pack();
            if (l.getWidth() > maxLabelWidth)
                maxLabelWidth = l.getWidth();
        }
        maxLabelWidth = Math.max(textwidth * 2, maxLabelWidth);
        for (OwnLabel l : labels)
            l.setWidth(maxLabelWidth);

        /** ADD ALL CONTENT **/
        tabContent.addActor(contentGraphics);
        tabContent.addActor(contentUI);
        tabContent.addActor(contentPerformance);
        tabContent.addActor(contentControls);
        tabContent.addActor(contentScreenshots);
        tabContent.addActor(contentFrames);
        tabContent.addActor(contentCamera);
        tabContent.addActor(content360);
        tabContent.addActor(contentData);
        tabContent.addActor(contentGaia);

        /** ADD TO MAIN TABLE **/
        content.add(tabContent).left().padLeft(10).expand().fill();

        // Listen to changes in the tab button checked states
        // Set visibility of the tab content to match the checked state
        ChangeListener tab_listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                contentGraphics.setVisible(tabGraphics.isChecked());
                contentUI.setVisible(tabUI.isChecked());
                contentPerformance.setVisible(tabPerformance.isChecked());
                contentControls.setVisible(tabControls.isChecked());
                contentScreenshots.setVisible(tabScreenshots.isChecked());
                contentFrames.setVisible(tabFrames.isChecked());
                contentCamera.setVisible(tabCamera.isChecked());
                content360.setVisible(tab360.isChecked());
                contentData.setVisible(tabData.isChecked());
                contentGaia.setVisible(tabGaia.isChecked());
            }
        };
        tabGraphics.addListener(tab_listener);
        tabUI.addListener(tab_listener);
        tabPerformance.addListener(tab_listener);
        tabControls.addListener(tab_listener);
        tabScreenshots.addListener(tab_listener);
        tabFrames.addListener(tab_listener);
        tabCamera.addListener(tab_listener);
        tab360.addListener(tab_listener);
        tabData.addListener(tab_listener);
        tabGaia.addListener(tab_listener);

        // Let only one tab button be checked at a time
        ButtonGroup<Button> tabs = new ButtonGroup<Button>();
        tabs.setMinCheckCount(1);
        tabs.setMaxCheckCount(1);
        tabs.add(tabGraphics);
        tabs.add(tabUI);
        tabs.add(tabPerformance);
        tabs.add(tabControls);
        tabs.add(tabScreenshots);
        tabs.add(tabFrames);
        tabs.add(tabCamera);
        tabs.add(tab360);
        tabs.add(tabData);
        tabs.add(tabGaia);

    }

    @Override
    protected void accept() {
        saveCurrentPreferences();
    }

    @Override
    protected void cancel() {
    }

    private void saveCurrentPreferences() {

    }

    private void selectFullscreen(boolean fullscreen, OwnTextField widthField, OwnTextField heightField, SelectBox<DisplayMode> fullScreenResolutions, CheckBox resizable, OwnLabel widthLabel, OwnLabel heightLabel) {
        if (fullscreen) {
            GlobalConf.screen.SCREEN_WIDTH = ((DisplayMode) fullScreenResolutions.getSelected()).width;
            GlobalConf.screen.SCREEN_HEIGHT = ((DisplayMode) fullScreenResolutions.getSelected()).height;
        } else {
            GlobalConf.screen.SCREEN_WIDTH = Integer.parseInt(widthField.getText());
            GlobalConf.screen.SCREEN_HEIGHT = Integer.parseInt(heightField.getText());
        }

        enableComponents(!fullscreen, widthField, heightField, resizable, widthLabel, heightLabel);
        enableComponents(fullscreen, fullScreenResolutions);
    }

    private int idxAa(int base, int x) {
        if (x == -1)
            return 1;
        if (x == -2)
            return 2;
        if (x == 0)
            return 0;
        return (int) (Math.log(x) / Math.log(2) + 1e-10) + 2;
    }

    private int idxLang(String code, LangComboBoxBean[] langs) {
        if (code.isEmpty()) {
            code = I18n.bundle.getLocale().toLanguageTag();
        }
        for (int i = 0; i < langs.length; i++) {
            if (langs[i].locale.toLanguageTag().equals(code)) {
                return i;
            }
        }
        return -1;
    }

    private String keysToString(TreeSet<Integer> keys) {
        String s = "";

        int i = 0;
        int n = keys.size();
        for (Integer key : keys) {
            s += Keys.toString(key).toUpperCase();
            if (i < n - 1) {
                s += " + ";
            }

            i++;
        }

        return s;
    }

    @Override
    public void notify(Events event, Object... data) {
        // TODO Auto-generated method stub

    }

}