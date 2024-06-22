package vlad.mester.syncroflowbe;

import vlad.mester.syncroflowbe.base.Rule;

public class Scheduler implements RuleControllerObserver {

    private int interval;
    private final RuleController ruleController;
    private Thread schedulerThread;

    public Scheduler(int interval, String id) {
        this.interval = interval;
        this.ruleController = RuleController.getInstance(id);
    }

    public void start() {
        ruleController.addObserver(this);
        schedulerThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(interval * 1000);
                    System.out.println("Checking rules");
                    checkRules();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        schedulerThread.start();
    }

    public void stop() {
        ruleController.removeObserver(this);
        if (schedulerThread != null && schedulerThread.isAlive()) {
            schedulerThread.interrupt();
            try {
                schedulerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkRules() {
        synchronized (ruleController) {
            for (Rule rule : ruleController.getRules()) {
                System.out.println("Checking rule: " + rule.getName());
                if (!rule.isActive()) {
                    System.out.println("Rule " + rule.getName() + " is inactive");
                    continue;
                }
                if (!(rule.getLastUse() == null || rule.getLastUse().getTime() + rule.getSleepTime() * 1000 <= System.currentTimeMillis())) {
                    System.out.println("Rule " + rule.getName() + " is sleeping");
                    continue;
                }

                if (!rule.isMultiUse() && rule.getLastUse() != null) {
                    System.out.println("Rule " + rule.getName() + " is not multiuse and has already been used");
                    continue;
                }

                if (!ruleController.getTriggerByName(rule.getTrigger()).evaluate()) {
                    System.out.println("Rule " + rule.getName() + " has untriggered triggers");
                    continue;
                }

                if (!ruleController.getActionByName(rule.getAction()).execute()) {
                    System.out.println("Rule " + rule.getName() + " failed to execute action");
                    continue;
                }
                rule.setLastUse();
            }
        }
    }

    @Override
    public void update(RuleController ruleController) {

    }
}
