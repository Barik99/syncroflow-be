package vlad.mester.syncroflowbe;

import vlad.mester.syncroflowbe.base.Rule;

import java.util.List;

public interface RuleUpdateListener {
    void updateRules(List<Rule> rules);
}
