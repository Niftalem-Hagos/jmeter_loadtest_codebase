import org.apache.jmeter.control.LoopController
import org.apache.jmeter.control.TransactionController
import org.apache.jmeter.control.gui.TestPlanGui
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import org.apache.jmeter.reporters.ResultCollector
import org.apache.jmeter.reporters.Summariser
import org.apache.jmeter.reporters.gui.ResultCollectorGui
import org.apache.jmeter.samplers.SampleSaveConfiguration
import org.apache.jmeter.testelement.TestPlan
import org.apache.jmeter.threads.ThreadGroup
import org.apache.jmeter.util.JMeterUtils

JMeterUtils.loadJMeterProperties("jmeter.properties")

def testPlan = new TestPlan("Delta Load Test")

def threadGroup = new ThreadGroup()
threadGroup.numThreads = 10
threadGroup.rampUp = 5
threadGroup.duration = 300

def loopController = new LoopController()
loopController.loops = 1
threadGroup.controller = loopController

testPlan.addThreadGroup(threadGroup)

def transactionController = new TransactionController()
transactionController.name = "Delta_Transaction"
transactionController.parent = threadGroup
threadGroup.addController(transactionController)

def httpSampler = new HTTPSampler()
httpSampler.domain = "www.delta.com"
httpSampler.protocol = "https"
httpSampler.method = "GET"
httpSampler.path = "/"
transactionController.addSampler(httpSampler)

def summariser = new Summariser("Delta Load Test Summary")
def resultCollector = new ResultCollector(summariser)
resultCollector.name = "Delta_Result_Collector"

def sampleSaveConfiguration = new SampleSaveConfiguration()
sampleSaveConfiguration.time = true
sampleSaveConfiguration.latency = true
sampleSaveConfiguration.connectTime = true
sampleSaveConfiguration.bytes = true
sampleSaveConfiguration.sentBytes = true
sampleSaveConfiguration.label = true
sampleSaveConfiguration.responseCode = true
sampleSaveConfiguration.responseMessage = true
sampleSaveConfiguration.successful = true
sampleSaveConfiguration.assertions = true
sampleSaveConfiguration.subresults = true
sampleSaveConfiguration.responseHeaders = true
sampleSaveConfiguration.requestHeaders = true
sampleSaveConfiguration.responseData = true
sampleSaveConfiguration.samplerData = true

resultCollector.setSaveConfig(sampleSaveConfiguration)

testPlan.addResultCollector(resultCollector)

def testPlanGui = new TestPlanGui(testPlan)

def engine = new StandardJMeterEngine()
engine.configure(testPlan)
engine.run()

