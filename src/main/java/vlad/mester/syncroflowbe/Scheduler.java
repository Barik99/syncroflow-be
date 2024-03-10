package vlad.mester.syncroflowbe;

import vlad.mester.syncroflowbe.base.Rule;

public class Scheduler implements RuleControllerObserver {

    private int interval;
    private final RuleController ruleController;
    private Thread schedulerThread;

    public Scheduler(int interval) {
        this.interval = interval;
        this.ruleController = RuleController.getInstance();
    }

    public void start() {
        ruleController.addObserver(this);
        schedulerThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(interval * 1000);
                    checkRules();
                } catch (InterruptedException e) {
                    // Thread interrupted, exit the loop
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
                if (!rule.isActive()) {
                    continue; // Skip inactive rules
                }

                long lastUseTime = rule.getLastUse() != null ? rule.getLastUse().getTime() : 0;
                if (lastUseTime + rule.getSleepTime() * 1000 <= System.currentTimeMillis()) {
                    continue; // Skip rules not ready for evaluation
                }

                if (!rule.getTrigger().evaluate()) {
                    continue; // Skip rules with untriggered triggers
                }

                if (!rule.getAction().execute()) {
                    continue; // Skip failed actions
                }
                rule.setLastUse();
            }
        }
    }

    @Override
    public void update(RuleController ruleController) {

    }
}
