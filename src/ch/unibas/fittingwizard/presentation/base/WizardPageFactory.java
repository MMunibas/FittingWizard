/*
 * Copyright (c) 2015, Florent Hedin, Markus Meuwly, and the University of Basel
 * All rights reserved.
 *
 * The 3-clause BSD license is applied to this software.
 * see LICENSE.txt
 *
 */
package ch.unibas.fittingwizard.presentation.base;

import ch.unibas.charmmtools.generate.CHARMM_InOut;
import ch.unibas.charmmtools.generate.inputs.CHARMM_Generator_DGHydr;
import ch.unibas.charmmtools.gui.RunningCHARMM_DG;
import ch.unibas.charmmtools.gui.RunningCHARMM_DenVap;
import ch.unibas.charmmtools.gui.database.DB_View_Edit_add;
import ch.unibas.charmmtools.gui.loadOutput.CHARMM_GUI_LoadOutput;
import ch.unibas.charmmtools.gui.step1.mdAssistant.CHARMM_GUI_InputAssistant;
import ch.unibas.charmmtools.gui.step2.showOutput.CHARMM_GUI_ShowOutput;
import ch.unibas.charmmtools.gui.step3.showResults.CHARMM_GUI_ShowResults;
import ch.unibas.charmmtools.gui.step4.ParGrid.CHARMM_GUI_Fitgrid;
import ch.unibas.charmmtools.gui.topology.GenerateTopology;
import ch.unibas.charmmtools.scripts.CHARMMScript_DG_gas;
import ch.unibas.charmmtools.scripts.CHARMMScript_DG_solvent;
import ch.unibas.charmmtools.scripts.CHARMMScript_Den_Vap;
import ch.unibas.charmmtools.scripts.ICHARMMScript;
import ch.unibas.charmmtools.workflows.RunCHARMMWorkflow;
import ch.unibas.fittingwizard.Settings;
import ch.unibas.fittingwizard.WhereToGo;
import ch.unibas.fittingwizard.application.Visualization;
import ch.unibas.fittingwizard.application.base.MoleculesDir;
import ch.unibas.fittingwizard.application.fitting.FitRepository;
import ch.unibas.fittingwizard.application.molecule.MoleculeRepository;
import ch.unibas.fittingwizard.application.scripts.babel.IBabelScript;
import ch.unibas.fittingwizard.application.scripts.export.IExportScript;
import ch.unibas.fittingwizard.application.scripts.fitmtp.FitMtpInput;
import ch.unibas.fittingwizard.application.scripts.fitmtp.IFitMtpScript;
import ch.unibas.fittingwizard.application.scripts.fittab.IFittabScript;
import ch.unibas.fittingwizard.application.scripts.lra.ILRAScript;
import ch.unibas.fittingwizard.application.scripts.multipolegauss.IMultipoleGaussScript;
import ch.unibas.fittingwizard.application.scripts.vmd.IVmdDisplayScript;
import ch.unibas.fittingwizard.application.tools.FitOutputParser;
import ch.unibas.fittingwizard.application.tools.GaussianLogModifier;
import ch.unibas.fittingwizard.application.tools.LPunParser;
import ch.unibas.fittingwizard.application.tools.Notifications;
import ch.unibas.fittingwizard.application.tools.charges.ChargesFileParser;
import ch.unibas.fittingwizard.application.workflows.ExportFitWorkflow;
import ch.unibas.fittingwizard.application.workflows.RunFitWorkflow;
import ch.unibas.fittingwizard.application.workflows.RunGaussianWorkflow;
import ch.unibas.fittingwizard.application.workflows.RunVmdDisplayWorkflow;
import ch.unibas.fittingwizard.infrastructure.RealBabelScript;
import ch.unibas.fittingwizard.infrastructure.RealExportScript;
import ch.unibas.fittingwizard.infrastructure.RealFitScript;
import ch.unibas.fittingwizard.infrastructure.RealFittabMarkerScript;
import ch.unibas.fittingwizard.infrastructure.RealLRAScript;
import ch.unibas.fittingwizard.infrastructure.RealMultipoleGaussScript;
import ch.unibas.fittingwizard.infrastructure.RealVmdDisplayScript;
import ch.unibas.fittingwizard.mocks.MockBabelScript;
import ch.unibas.fittingwizard.mocks.MockExportScript;
import ch.unibas.fittingwizard.mocks.MockFitMtpScript;
import ch.unibas.fittingwizard.mocks.MockFittabMarkerScript;
import ch.unibas.fittingwizard.mocks.MockLRAScript;
import ch.unibas.fittingwizard.mocks.MockMultipoleGaussScript;
import ch.unibas.fittingwizard.presentation.MoleculeListPage;
import ch.unibas.fittingwizard.presentation.addmolecule.AtomChargesDto;
import ch.unibas.fittingwizard.presentation.addmolecule.AtomTypeChargePage;
import ch.unibas.fittingwizard.presentation.addmolecule.CoordinatesDto;
import ch.unibas.fittingwizard.presentation.addmolecule.CoordinatesPage;
import ch.unibas.fittingwizard.presentation.addmolecule.GaussCalculationDto;
import ch.unibas.fittingwizard.presentation.addmolecule.GaussCalculationPage;
import ch.unibas.fittingwizard.presentation.addmolecule.MultipoleGaussParameterDto;
import ch.unibas.fittingwizard.presentation.addmolecule.MultipoleGaussParameterPage;
import ch.unibas.fittingwizard.presentation.addmolecule.SelectCoordinateFilePage;
import ch.unibas.fittingwizard.presentation.fitting.EditAtomTypeChargesDialog;
import ch.unibas.fittingwizard.presentation.fitting.FitResultPage;
import ch.unibas.fittingwizard.presentation.fitting.FittingParameterPage;
import ch.unibas.fittingwizard.presentation.fitting.RunningFitPage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * User: mhelmer Date: 26.11.13 Time: 11:31
 */
public class WizardPageFactory {

    private static final Logger logger = Logger.getLogger(WizardPageFactory.class);

    private Settings settings;
    private Visualization visualization;
    private Notifications notifications;
    private MoleculeRepository moleculeRepository;
    private FitRepository fitRepository;
    private DefaultValues defaultValues;
    private File sessionDir;
    private MoleculesDir moleculesDir;

    private LPunParser lPunParser;
    private EditAtomTypeChargesDialog editAtomTypeChargesDialog;

    private IMultipoleGaussScript gaussScript;
    private IBabelScript babelScript;
    private ILRAScript lraScript;
    private IFittabScript fittabMarkerScript;
    private IFitMtpScript fitMtpScript;
    private IExportScript exportScript;
    private IVmdDisplayScript vmdScript;
    private ICHARMMScript charmmScript_Den_Vap, charmmScript_DG_gas, charmmScript_DG_solvent, charmmScript_default;

    private RunFitWorkflow runFitWorkflow;
    private ExportFitWorkflow exportFitWorkflow;
    private RunGaussianWorkflow runGaussianWorkflow;
    private RunVmdDisplayWorkflow vmdDisplayWorkflow;
    private RunCHARMMWorkflow charmmWorkflow_Den_Vap, charmmWorkflow_DG, charmmWorkflow_other;

    public WizardPageFactory(Stage primaryStage) {
        initializeDependencies(primaryStage);
    }

    private void initializeDependencies(Stage primaryStage) {
        this.settings = Settings.loadConfig();
        this.sessionDir = initializeCurrentSessionDirectory(settings.getDataDir());
        this.moleculesDir = new MoleculesDir(settings.getMoleculeDir());

        this.visualization = new Visualization(primaryStage);

        // TODO use repo to persist data into state dir
        this.moleculeRepository = new MoleculeRepository();
        this.fitRepository = new FitRepository(moleculeRepository);

        this.defaultValues = new DefaultValues(settings);
        // TODO fill from settings.
        notifications = new Notifications(settings.getProperties());
//        notifications.sendTestMail();
//        notifications.sendLogMail();
        lPunParser = new LPunParser(settings.getMoleculeDir());
        initializeScripts();
        initializeWorkflows();

        editAtomTypeChargesDialog = new EditAtomTypeChargesDialog();
    }

    private void initializeScripts() {
        File moleculeDestination = moleculesDir.getDirectory();
        File moleculeTestdataDir = settings.getMoleculeTestdataDir();
        File outputDir = new File(sessionDir, RealFitScript.OutputDirName);

        if (settings.getValue("mocks.enabled").equals("false")) {
            babelScript = new RealBabelScript(moleculeDestination);
            lraScript = new RealLRAScript(settings);
            fittabMarkerScript = new RealFittabMarkerScript(moleculeDestination, settings);

            fitMtpScript = new RealFitScript(sessionDir, moleculeDestination, settings);
            exportScript = new RealExportScript(settings, outputDir, moleculeDestination);
            vmdScript = new RealVmdDisplayScript(settings, outputDir, moleculeDestination);
        } else {
            babelScript = new MockBabelScript(moleculeDestination, moleculeTestdataDir);
            lraScript = new MockLRAScript(moleculeDestination, moleculeTestdataDir);
            fittabMarkerScript = new MockFittabMarkerScript(moleculeDestination, moleculeTestdataDir);

            fitMtpScript = new MockFitMtpScript(sessionDir, settings.getTestdataDir());
            exportScript = new MockExportScript(sessionDir, settings.getTestdataDir());
        }

        if (settings.getValue("mocks.use_gaussian_mock").equals("true")) {
            gaussScript = new MockMultipoleGaussScript(moleculeDestination, moleculeTestdataDir);
        } else {
            gaussScript = new RealMultipoleGaussScript(moleculesDir, settings);
        }

        charmmScript_Den_Vap = new CHARMMScript_Den_Vap(sessionDir, settings);
        charmmScript_DG_gas = new CHARMMScript_DG_gas(sessionDir, settings);
        charmmScript_DG_solvent = new CHARMMScript_DG_solvent(sessionDir, settings);
        charmmScript_default = new CHARMMScript_Den_Vap(sessionDir, settings);
    }

    private void initializeWorkflows() {
        runFitWorkflow = new RunFitWorkflow(fitMtpScript,
                fitRepository,
                new ChargesFileParser(),
                new FitOutputParser());

        exportFitWorkflow = new ExportFitWorkflow(exportScript, sessionDir);

        runGaussianWorkflow = new RunGaussianWorkflow(gaussScript,
                babelScript,
                lraScript,
                fittabMarkerScript,
                new GaussianLogModifier(),
                notifications);

        vmdDisplayWorkflow = new RunVmdDisplayWorkflow(vmdScript, sessionDir);

        charmmWorkflow_Den_Vap = new RunCHARMMWorkflow(charmmScript_Den_Vap);
        charmmWorkflow_DG = new RunCHARMMWorkflow(charmmScript_DG_gas, charmmScript_DG_solvent);
        charmmWorkflow_other = new RunCHARMMWorkflow(charmmScript_default);
    }

    public <T extends WizardPage> WizardPage create(Class<T> type, Object parameter) {
        WizardPage page;
        logger.info("Type is : " + type.toString());
        try {
            if (type == MoleculeListPage.class) {
                page = new MoleculeListPage(visualization,
                        moleculeRepository,
                        fitRepository,
                        moleculesDir);
            } // ADD MOLECULE PAGES
            else if (type == SelectCoordinateFilePage.class) {
                File dto = (File) parameter;
                page = new SelectCoordinateFilePage(dto);
            } else if (type == CoordinatesPage.class) {
                CoordinatesDto dto = throwIfParameterIsNull(parameter);
                page = new CoordinatesPage(visualization, moleculesDir, dto);
            } else if (type == MultipoleGaussParameterPage.class) {
                MultipoleGaussParameterDto dto = throwIfParameterIsNull(parameter);
                page = new MultipoleGaussParameterPage(defaultValues, dto);
            } else if (type == AtomTypeChargePage.class) {
                AtomChargesDto dto = throwIfParameterIsNull(parameter);
                page = new AtomTypeChargePage(moleculeRepository, lPunParser, visualization, dto);
            } else if (type == GaussCalculationPage.class) {
                GaussCalculationDto dto = throwIfParameterIsNull(parameter);
                page = new GaussCalculationPage(runGaussianWorkflow, dto);
//                page = new ProgressWithQstatTest();
            } // GAUSSIAN FITTING PAGES
            else if (type == FittingParameterPage.class) {
                FitMtpInput dto = (FitMtpInput) parameter;
                page = new FittingParameterPage(fitRepository,
                        moleculeRepository,
                        defaultValues,
                        sessionDir,
                        editAtomTypeChargesDialog,
                        dto);
            } else if (type == RunningFitPage.class) {
                FitMtpInput dto = throwIfParameterIsNull(parameter);
                page = new RunningFitPage(runFitWorkflow, dto);
            } else if (type == FitResultPage.class) {
                page = new FitResultPage(moleculeRepository,
                        fitRepository,
                        visualization,
                        exportFitWorkflow,
                        vmdDisplayWorkflow);
            } // CHARMM FITTING PAGES
            else if (type == RunningCHARMM_DenVap.class) {
                List<CHARMM_InOut> ioList = throwIfParameterIsNull(parameter);
                page = new RunningCHARMM_DenVap(charmmWorkflow_Den_Vap, ioList);
            } else if (type == RunningCHARMM_DG.class) {
                List<CHARMM_Generator_DGHydr> dgList = throwIfParameterIsNull(parameter);
                page = new RunningCHARMM_DG(charmmWorkflow_DG, dgList);
            } else if (type == CHARMM_GUI_InputAssistant.class) {
                if (parameter == null) {
                    page = new CHARMM_GUI_InputAssistant(charmmWorkflow_Den_Vap);
                } else {
//                    List<CHARMM_InOut> ioList = throwIfParameterIsNull(parameter);
//                    page = new CHARMM_GUI_InputAssistant(charmmWorkflow_Den_Vap, ioList);
                    List<File> ioList = throwIfParameterIsNull(parameter);
                    page = new CHARMM_GUI_InputAssistant(charmmWorkflow_Den_Vap, ioList);
                }
            } else if (type == CHARMM_GUI_ShowOutput.class) {
                List<CHARMM_InOut> ioList = throwIfParameterIsNull(parameter);
                page = new CHARMM_GUI_ShowOutput(charmmWorkflow_Den_Vap, ioList);
            } else if (type == CHARMM_GUI_ShowResults.class) {
                List<CHARMM_InOut> ioList = throwIfParameterIsNull(parameter);
                page = new CHARMM_GUI_ShowResults(charmmWorkflow_Den_Vap, ioList);
            } else if (type == CHARMM_GUI_Fitgrid.class) {
                page = new CHARMM_GUI_Fitgrid(charmmWorkflow_other);
            } else if (type == DB_View_Edit_add.class) {
                page = new DB_View_Edit_add(settings);
            } else if (type == WhereToGo.class) {
                page = new WhereToGo();
            } else if (type == GenerateTopology.class) {
                page = new GenerateTopology(charmmWorkflow_other);
            } else if (type == CHARMM_GUI_LoadOutput.class) {
                page = new CHARMM_GUI_LoadOutput();
            } // MISC
            else {
                page = type.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not create instance of page " + type.getSimpleName(), e);
        }
        return page;
    }

    private <T> T throwIfParameterIsNull(Object parameter) {
        T casted = (T) parameter;
        if (casted == null) {
            throw new RuntimeException("Expected parameter object is null.");
        }
        return casted;
    }

    private File initializeCurrentSessionDirectory(File dataDir) {
        String sessionName = getSessionName();
        File sessionDir = new File(dataDir, sessionName);
        logger.info("Creating session directory " + sessionDir.getAbsolutePath());

        boolean directoryCreated = sessionDir.mkdirs();

        if (!directoryCreated) {
            throw new RuntimeException("Could not create session directory.");
        }

        return sessionDir;
    }

    private String getSessionName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date());
    }

    public Settings getSettings() {
        return settings;
    }
}
