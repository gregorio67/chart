import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;

import ncd.spring.common.exception.BizException;

public class MessageRefresher
{
  public static final void refresh(MessageSource messageSource)
  {
    if ((messageSource instanceof RefreshableMessageSource))
    {
      ((RefreshableMessageSource)messageSource).refresh();
    }
    else if ((messageSource instanceof DelegatingMessageSource))
    {
      MessageSource parentMessageSource = ((DelegatingMessageSource)messageSource).getParentMessageSource();
      if (parentMessageSource != null) {
        refresh(parentMessageSource);
      }
    }
    else
    {
      throw new BizException("Refresh is possible only RefreshableMessageSource type.");
    }
  }
  
  public static final void refreshIncludingAncestors(MessageSource messageSource)
  {
    if ((messageSource instanceof RefreshableMessageSource))
    {
      ((RefreshableMessageSource)messageSource).refreshIncludingAncestors();
    }
    else if ((messageSource instanceof DelegatingMessageSource))
    {
      MessageSource parentMessageSource = ((DelegatingMessageSource)messageSource).getParentMessageSource();
      if (parentMessageSource != null) {
        refreshIncludingAncestors(parentMessageSource);
      }
    }
    else
    {
      throw new BizException("Refresh is possible only RefreshableMessageSource type.");
    }
  }
}
